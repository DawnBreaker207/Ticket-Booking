package com.dawn.booking.service;

import com.dawn.common.core.exception.wrapper.RedisStorageException;
import com.dawn.common.core.helper.RedisKeyHelper;
import com.dawn.common.infra.redis.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationRedisService {

    private final RedisService redisService;
    private final RedisPublisher redisPublisher;
    private final ObjectMapper mapper;


    //    Reservation data
    public void saveReservationInit(String reservationId, Map<String, String> data, Duration ttl) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        redisService.putHash(key, data, ttl);
    }

    public Long getReservationTtl(String reservationId) {
        return redisService.getExpired(RedisKeyHelper.reservationHoldKey(reservationId));
    }

    public Map<Object, Object> getReservationData(String reservationId) {
        return redisService.getHash(RedisKeyHelper.reservationHoldKey(reservationId));
    }

    public void updateReservationSeats(String reservationId, List<Long> seats) {
        try {
            String key = RedisKeyHelper.reservationHoldKey(reservationId);
            redisService.put(key, "seats", mapper.writeValueAsString(seats));
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize seats for reservation {}: {}", reservationId, e.getMessage(), e);
        }
    }

    public void deleteReservation(String reservationId) {
        String key = RedisKeyHelper.reservationHoldKey(reservationId);
        redisService.delete(key);
    }

    //    Seat locking
    public Boolean lockSeat(Long seatId, String ownerKey, Duration ttl) {
        return redisService.setIfAbsent(RedisKeyHelper.seatLockKey(seatId), ownerKey, ttl);
    }

    public String getSeatOwner(Long seatId) {
        Object val = redisService.get(RedisKeyHelper.seatLockKey(seatId));
        return val != null ? String.valueOf(val) : null;
    }

    public void refreshSeatLockIfOwner(Long seatId, String ownerKey, Duration ttl) {
        String key = RedisKeyHelper.seatLockKey(seatId);
        String current = getSeatOwner(seatId);
        if (ownerKey.equals(current)) {
            redisService.expire(key, ttl);
        }
    }

    public void releaseSeat(Long seatId) {
        redisService.delete(RedisKeyHelper.seatLockKey(seatId));
    }


    public Boolean deleteSeatLockIfOwner(Long seatId, String expectedOwner) {
        String key = RedisKeyHelper.seatLockKey(seatId);
        String current = (String) redisService.get(key);
        if (expectedOwner.equals(current)) {
            return redisService.delete(key);
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
