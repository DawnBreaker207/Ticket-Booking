package com.example.backend.exception;

import com.example.backend.exception.payload.ExceptionMessage;
import com.example.backend.exception.wrapper.*;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(ApiExceptionHandler.class);
    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            UserEmailExistedException.class,
            UserPasswordNotMatchException.class,
            UsernameExistedException.class,
            MovieNotFoundException.class,
            CinemaHallNotFoundException.class,
            OrderNotFoundException.class,
            MovieExistedException.class,
            ForbiddenPermissionException.class,
            OrderExpiredException.class,
            SeatUnavailableException.class,
            RedisStorageException.class,
            RefreshTokenNotFoundException.class,
            RefreshTokenExpiredException.class
    })
    public <T extends RuntimeException> ResponseEntity<ExceptionMessage> handleApiRequestException(final T e) {
        log.info("**ApiExceptionHandler controller, handler API request*\n");
        final var status = (e instanceof ApiException ae && ae.getStatus() != null) ? ae.getStatus() : DEFAULT_STATUS;
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), status,
                "#### " + e.getMessage() + "! ####"), status);

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handlerValidationException(MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), DEFAULT_STATUS, errorMsg), DEFAULT_STATUS);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessage> handlerConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations()
                .stream()
                .map(err -> err.getPropertyPath() + ": " + err.getMessage())
                .findFirst()
                .orElse("Invalid parameter");
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), DEFAULT_STATUS, errorMsg), DEFAULT_STATUS);
    }
}
