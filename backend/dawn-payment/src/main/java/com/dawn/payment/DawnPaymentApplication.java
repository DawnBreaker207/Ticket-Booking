package com.dawn.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dawn")
public class DawnPaymentApplication {

    public static void main(String[] args) {
        SpringApplication.run(DawnPaymentApplication.class, args);
    }

}
