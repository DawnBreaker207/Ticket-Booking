package com.example.backend.util;

import java.util.Random;
import java.util.UUID;

public class OrderUtils {

    private static final Random random = new Random();

    public static String generateOrderIds() {
	String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase();
	return String.format("ORD-" + uuid);
    }
}
