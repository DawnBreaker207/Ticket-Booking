package com.dawn.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dawn")
public class DawnNotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DawnNotificationApplication.class, args);
    }

}
