package com.example.backend.helper;

public class RedisKeyHelper {
    public static String reservationHoldKey(final String reservationId) {
        return "reservationId:" + reservationId;
    }

    public static String seatLockKey(final Long seatId) {
        return "seat:locked:" + seatId;
    }
}
