package com.example.backend.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
@Slf4j
public class RedisListenerRegistrar implements InitializingBean {

    private final RedisMessageListenerContainer redisContainer;
    private final RedisSubscriber redisSubscriber;


    @Override
    public void afterPropertiesSet() throws Exception {
        redisContainer.addMessageListener(redisSubscriber, new PatternTopic("channel:reservation:*"));
        redisContainer.addMessageListener(redisSubscriber, new PatternTopic("channel:showtime:*"));
        log.info("Redis Subscriber registered to showtime & reservation channels");
    }
}
