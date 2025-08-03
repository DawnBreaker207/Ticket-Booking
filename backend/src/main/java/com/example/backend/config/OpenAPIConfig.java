package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI ServiceAPI() {
	return new OpenAPI()
		.info(new Info()
			.title("Booking Ticket API")
			.description("This is the REST API for Booking Ticket service")
			.version("v0.0.1")
			.license(new License()
				.name("Apache 2.0")))
			.externalDocs(new ExternalDocumentation()
				.description("You can refer to Booking Ticket Wiki Documentation")
				.url("https://booking-ticket-dummy-url.com/docs"));
    }
}
