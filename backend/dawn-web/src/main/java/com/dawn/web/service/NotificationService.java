package com.dawn.web.service;

import com.dawn.common.utils.BarcodeUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final JavaMailSender mailSender;

    private final TemplateEngine templateEngine;

    private final Map<String, Map<String, SseEmitter>> emitters = new ConcurrentHashMap<>();

    private final ObjectMapper objectMapper;

    private final RedisTemplate<String, Object> redisTemplate;

    public SseEmitter subscribe(String channel, String clientId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitter.onCompletion(() -> removeEmitter(channel, clientId));
        emitter.onTimeout(() -> removeEmitter(channel, clientId));
        emitter.onError((e) -> removeEmitter(channel, clientId));
        try {
            emitters.computeIfAbsent(channel, c -> new ConcurrentHashMap<>()).put(clientId, emitter);
            log.info("Subscribe to channel {}", channel);
            if (channel.startsWith("channel:showtime:")) {
                sendCurrentSeatState(emitter, channel);
            }
            log.info("Subscribe to channel {}", emitter);
            return emitter;

        } catch (Exception e) {
            log.error("Failed to subscribe client [{}] to [{}]", clientId, channel, e);
            removeEmitter(channel, clientId);
            emitter.completeWithError(e);
            throw e;
        }
    }

    private void removeEmitter(String channel, String clientId) {
        Map<String, SseEmitter> channelEmitters = emitters.get(channel);
        if (channelEmitters != null) {
            channelEmitters.remove(clientId);
            if (channelEmitters.isEmpty()) {
                emitters.remove(channel);
                log.info("All clients disconnected from [{}]", channel);
            }
        } else {
            log.info("Client [{}] disconnected from [{}]", clientId, channel);
        }
    }


    private void sendCurrentSeatState(SseEmitter emitter, String channel) {
        try {
            String[] parts = channel.split(":");
            Long showtimeId = Long.valueOf(parts[2]);

            List<Map<String, Object>> holdSeats = getSeatSnapshot(showtimeId);

            Map<String, Object> initialState = Map.of(
                    "event", "SEAT_STATE_INIT",
                    "showtimeId", showtimeId,
                    "seatIds", holdSeats
            );

            String messageJson = objectMapper.writeValueAsString(initialState);
            emitter.send(SseEmitter.event().name("SEAT_STATE_INIT").data(messageJson));
            log.info("Sent initial seat state to new client, {} seats hold", holdSeats.size());
        } catch (Exception e) {
            log.debug("Emitter disconnected before receiving initial state.");
            log.error("Failed to send initial seat state", e);
            removeEmitter(channel, "anonymous");
        }
    }

    public List<Map<String, Object>> getSeatSnapshot(Long showtimeId) {
        List<Map<String, Object>> seatState = new ArrayList<>();
        String pattern = "seat:locked:*";

        redisTemplate.execute((RedisCallback<Void>) connection -> {
            ScanOptions options = ScanOptions.scanOptions()
                    .match(pattern)
                    .count(500)
                    .build();

            try (Cursor<byte[]> cursor = connection.keyCommands().scan(options)) {
                while (cursor.hasNext()) {
                    String key = new String(cursor.next(), StandardCharsets.UTF_8);

                    //     Take data from Redis
                    Object rawValue = redisTemplate.opsForValue().get(key);
                    if (rawValue == null) continue;

                    String reservationKey = rawValue.toString();
                    String reservationId = reservationKey.substring(reservationKey.lastIndexOf(":") + 1);

                    if (showtimeId != null) {
                        Map<Object, Object> reservationData = redisTemplate.opsForHash().entries(reservationKey);

                        if (reservationData.isEmpty()) continue;

                        String storedShowtimeId = (String) reservationData.get("showtimeId");

                        if (!showtimeId.toString().equals(storedShowtimeId)) {
                            continue;
                        }
                    }
                    String seatIdStr = key.substring(key.lastIndexOf(":") + 1);

                    Map<String, Object> initialState = Map.of(
                            "seatId", Long.parseLong(seatIdStr),
                            "reservationId", reservationId
                    );

                    seatState.add(initialState);
                }
            }
            return null;
        });

        return seatState;
    }

    public void broadcastToChannel(String channel, String message) {
        Map<String, SseEmitter> channelEmitters = emitters.get(channel);
        if (channelEmitters == null || channelEmitters.isEmpty()) return;
        log.info("Broadcasting to [{}] clients in channel [{}]", channelEmitters.size(), channel);
        log.debug("Message content [{}]", message);

        String eventName = extractEventName(message);
        log.info("Event name extracted: {}", eventName);
        channelEmitters.forEach((clientId, emitter) -> {
            try {
                emitter.send(SseEmitter
                        .event()
                        .name(eventName)
                        .data(message));
                log.info("Broadcast send event [{}] to client [{}] in channel [{}]", eventName, clientId, emitter);
            } catch (IOException e) {
                log.debug("Client [{}] disconnected during broadcast. Cleaning up.", clientId);
                removeEmitter(channel, clientId);
            }
        });
    }

    private String extractEventName(String messageJson) {
        try {
            log.info("Parsing event from: {}", messageJson);
            JsonNode node = objectMapper.readTree(messageJson);
            String eventName = node.has("event") ? node.get("event").asText() : "message";
            log.info("Extracted event name [{}]", eventName);
            return eventName;
        } catch (Exception e) {
            log.warn("Cannot parse event name from message", e);
            return "message";
        }
    }

    public void sendEmail(
            String to,
            String name,
            String reservationId,
            String movieName,
            String theaterName,
            String showtimeSession,
            String seats,
            String paymentTime,
            String total
    ) {
        log.info("Got message from reservation");
        String barcodeBase64 = BarcodeUtils.generateCode128(reservationId, 300, 100);

        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("demo@gmail.com");
            messageHelper.setTo(to);
            messageHelper.setSubject("[Thông tin vé phim] - Đặt vé trực tuyến thành công / Your online ticket purchase has been successful");

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("reservationId", reservationId);
            context.setVariable("movieName", movieName);
            context.setVariable("theaterName", theaterName);
            context.setVariable("showtimeSession", showtimeSession);
            context.setVariable("seats", seats);
            context.setVariable("paymentTime", paymentTime);
            context.setVariable("total", total);
            context.setVariable("barcode", barcodeBase64);

            String html = templateEngine.process("email", context);
            messageHelper.setText(html, true);
        };

        try {
            mailSender.send(messagePreparator);
            log.info("Email notification sent!");
        } catch (MailException ex) {
            log.error("Exception occurred when sending email", ex);
            throw new RuntimeException("Exception occurred when sending email to demo@gmail.com", ex);
        }

    }

}
