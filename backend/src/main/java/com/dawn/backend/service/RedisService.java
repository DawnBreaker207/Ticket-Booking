package com.dawn.backend.service;

import com.dawn.backend.config.redis.RedisPublisher;
import com.dawn.backend.exception.wrapper.RedisStorageException;
import com.dawn.backend.helper.RedisKeyHelper;
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

    public void saveReservation(String reservationId, Map<String, String> data, Duration ttl) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        redisTemplate.opsForHash().putAll(key, data);
        redisTemplate.expire(key, ttl);
    }

    public void publishSeatHold(Long showtimeId, Map<String, Object> event) {
        try {
            String channel = RedisKeyHelper.showtimeChannel(showtimeId);
            log.info("Publish to Redis channel [{}]: {}", channel, event);
            redisPublisher.publish(channel, event);
            log.info("Successfully publish event {} to channel {}", event.get("event"), channel);
        } catch (Exception ex) {
            log.error("Failed to serialize event", ex);
            throw new RedisStorageException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to store seat information. Please try again");
        }
    }

}
