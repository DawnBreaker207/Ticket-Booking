package com.dawn.booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dawn")
public class DawnBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(DawnBookingApplication.class, args);
    }

}
