package com.dawn.backend.helper;

public interface RedisKeyHelper {
    static String reservationHoldKey(final String reservationId) {
        return "reservation:data:" + reservationId;
    }

    static String seatLockKey(final Long seatId) {
        return "seat:locked:" + seatId;
    }

    static String showtimeChannel(final Long showtimeId) {
        return "channel:showtime:" + showtimeId;
    }
}
