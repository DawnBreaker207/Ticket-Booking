package com.dawn.booking.utils;

import java.util.UUID;

public class ReservationUtils {

    public static String generateReservationIds() {
        String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
        return String.format("ORD-" + uuid);
    }
}
