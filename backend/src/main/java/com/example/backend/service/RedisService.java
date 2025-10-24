package com.example.backend.service;

import com.example.backend.config.redis.RedisPublisher;
import com.example.backend.exception.wrapper.RedisStorageException;
import com.example.backend.helper.RedisKeyHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public void saveReservation(String reservationId, Map<String, String> data, Duration ttl) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        redisTemplate.opsForHash().putAll(key, data);
        redisTemplate.expire(key, ttl);
    }

    public void publishSeatHold(Long showtimeId, Map<String, Object> event) {
        try {
            String channel = RedisKeyHelper.showtimeChannel(showtimeId);
            log.info("Publish to Redis channel [{}]: {}", channel, event);
            String message = mapper.writeValueAsString(event);
            redisPublisher.publish(channel, message);
            log.info("Successfully publish event {} to channel {}", event.get("event"), channel);
        } catch (JsonProcessingException ex) {
            log.error("Failed to serialize event", ex);
            throw new RedisStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store seat information. Please try again");
        }
    }

}
