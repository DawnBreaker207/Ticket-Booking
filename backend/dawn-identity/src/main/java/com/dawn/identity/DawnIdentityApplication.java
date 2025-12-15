package com.dawn.identity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.dawn")
public class DawnIdentityApplication {

	public static void main(String[] args) {
		SpringApplication.run(DawnIdentityApplication.class, args);
	}

}
