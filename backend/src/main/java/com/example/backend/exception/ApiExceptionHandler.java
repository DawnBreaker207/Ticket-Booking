package com.example.backend.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.exception.payload.ExceptionMessage;
import com.example.backend.exception.wrapper.CinemaHallNotFoundException;
import com.example.backend.exception.wrapper.MovieExistedException;
import com.example.backend.exception.wrapper.MovieNotFoundException;
import com.example.backend.exception.wrapper.OrderNotFoundException;
import com.example.backend.exception.wrapper.UserNotFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = { UserNotFoundException.class, MovieNotFoundException.class,
	    CinemaHallNotFoundException.class, OrderNotFoundException.class, MovieExistedException.class })

    public <T extends RuntimeException> ResponseEntity<ExceptionMessage> handleApiRequestException(final T e) {
	log.info("**ApiExceptionHandler controller, handler API request*\n");
	final var badRequest = HttpStatus.BAD_REQUEST;
	return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), badRequest,
		"#### " + e.getMessage() + "! ####"), badRequest);

    }
}
