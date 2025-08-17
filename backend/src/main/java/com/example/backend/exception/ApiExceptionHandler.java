package com.example.backend.exception;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.example.backend.exception.wrapper.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.backend.exception.payload.ExceptionMessage;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            UserEmailExistedException.class,
            UserPasswordNotMatchException.class,
            MovieNotFoundException.class,
            CinemaHallNotFoundException.class,
            OrderNotFoundException.class,
            MovieExistedException.class,
            ForbiddenPermissionException.class,
            OrderExpiredException.class,
            SeatUnavailableException.class,
            RedisStorageException.class

    })

    public <T extends RuntimeException> ResponseEntity<ExceptionMessage> handleApiRequestException(final T e) {
        log.info("**ApiExceptionHandler controller, handler API request*\n");
        final var badRequest = HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), badRequest,
                "#### " + e.getMessage() + "! ####"), badRequest);

    }
}
