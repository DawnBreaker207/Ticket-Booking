package com.dawn.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
public class DawnCommonApplication {

    public static void main(String[] args) {
        SpringApplication.run(DawnCommonApplication.class, args);
    }

}
