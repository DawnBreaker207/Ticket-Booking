package com.dawn.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.dawn")
@EntityScan(basePackages = "com.dawn")
@EnableJpaRepositories(basePackages = "com.dawn")
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
public class DawnWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(DawnWebApplication.class, args);
    }

}