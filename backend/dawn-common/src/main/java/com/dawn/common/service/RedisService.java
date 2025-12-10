package com.dawn.common.service;

import com.dawn.common.config.redis.RedisPublisher;
import com.dawn.common.exception.wrapper.RedisStorageException;
import com.dawn.common.helper.RedisKeyHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisPublisher redisPublisher;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper mapper;


    //    Reservation data
    public void saveReservationInit(String reservationId, Map<String, String> data, Duration ttl) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        redisTemplate.opsForHash().putAll(key, data);
        redisTemplate.expire(key, ttl);
    }

    public Long getReservationTtl(String reservationId) {
        return redisTemplate.getExpire(RedisKeyHelper.reservationHoldKey(reservationId), TimeUnit.SECONDS);
    }

    public Map<Object, Object> getReservationData(String reservationId) {
        return redisTemplate.opsForHash().entries(RedisKeyHelper.reservationHoldKey(reservationId));
    }

    public void updateReservationSeats(String reservationId, List<Long> seats) {
        try {
            String key = RedisKeyHelper.reservationHoldKey(reservationId);
            redisTemplate.opsForHash().put(key, "seats", mapper.writeValueAsString(seats));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize seats for reservation {}: {}", reservationId, e.getMessage(), e);
        }
    }

    public void deleteReservation(String reservationId) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        redisTemplate.delete(key);
    }

    //    Seat locking
    public Boolean lockSeat(Long seatId, String ownerKey, Duration ttl) {
        Boolean ok = redisTemplate.opsForValue().setIfAbsent(RedisKeyHelper.seatLockKey(seatId), ownerKey, ttl);
        return ok != null && ok;
    }

    public String getSeatOwner(Long seatId) {
        Object val = redisTemplate.opsForValue().get(RedisKeyHelper.seatLockKey(seatId));
        return val != null ? String.valueOf(val) : null;
    }

    public void refreshSeatLockIfOwner(Long seatId, String ownerKey, Duration ttl) {
        String key = RedisKeyHelper.seatLockKey(seatId);
        String current = (String) redisTemplate.opsForValue().get(key);
        if (ownerKey.equals(current)) {
            redisTemplate.expire(key, ttl);
        }
    }

    public void releaseSeat(Long seatId) {
        redisTemplate.delete(RedisKeyHelper.seatLockKey(seatId));
    }


    public Boolean deleteSeatLockIfOwner(Long seatId, String expectedOwner) {
        String key = RedisKeyHelper.seatLockKey(seatId);
        String current = (String) redisTemplate.opsForValue().get(key);
        if (expectedOwner.equals(current)) {
            return redisTemplate.delete(key);
        }
        return false;
    }

    //    Event publish
    public void publishSeatEvent(Long showtimeId, Map<String, Object> event) {
        try {
            String channel = RedisKeyHelper.showtimeChannel(showtimeId);
            log.info("Publish to Redis channel [{}]: {}", channel, event);
            redisPublisher.publish(channel, event);
            log.info("Successfully publish event {} to channel {}", event.get("event"), channel);
        } catch (Exception ex) {
            log.error("Failed to serialize event", ex);
            throw new RedisStorageException("Failed to store seat information. Please try again");
        }
    }


}
