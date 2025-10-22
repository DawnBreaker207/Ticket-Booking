package com.example.backend.config.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;

    public void publish(String channel, Object message) {
        try {
            String payload = (message instanceof String)
                    ? (String) message
                    : mapper.writeValueAsString(message);
            redisTemplate.convertAndSend(channel, payload);
            log.info("Successfully publish event {} to channel {}", message.getClass().getSimpleName(), channel);
        } catch (Exception e) {
            log.error("Failed to publish message", e);
        }
    }
}
