package com.example.backend.helper;

public class RedisKeyHelper {
    public static String orderHoldKey(String orderId) {
	return "" + orderId;
    }

    public static String seatLockKey(Long seatId) {
	return "seat:locked:" + seatId;
    }
}
