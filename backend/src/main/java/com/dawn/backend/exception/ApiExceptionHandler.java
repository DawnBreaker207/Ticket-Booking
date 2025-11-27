package com.dawn.backend.exception;

import com.dawn.backend.exception.payload.ExceptionMessage;
import com.dawn.backend.exception.wrapper.*;
import com.dawn.backend.exception.wrapper.*;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private static final HttpStatus DEFAULT_STATUS = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(value = {
            UserNotFoundException.class,
            UserEmailExistedException.class,
            UserEmailNotFoundException.class,
            UserPasswordNotMatchException.class,
            UsernameExistedException.class,
            MovieNotFoundException.class,
            TheaterNotFoundException.class,
            ReservationNotFoundException.class,
            MovieExistedException.class,
            ForbiddenPermissionException.class,
            ReservationExpiredException.class,
            SeatUnavailableException.class,
            RedisStorageException.class,
            RefreshTokenNotFoundException.class,
            RefreshTokenExpiredException.class,
            RoleNotFoundException.class,
    })
    public <T extends RuntimeException> ResponseEntity<ExceptionMessage> handleApiRequestException(final T e) {
        log.info("**ApiExceptionHandler controller, handler API request*\n");
        final var status = (e instanceof ApiException ae && ae.getStatus() != null) ? ae.getStatus() : DEFAULT_STATUS;
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), status,
                "#### " + e.getMessage() + "! ####"), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionMessage> handlerValidationException(final MethodArgumentNotValidException ex) {
        String errorMsg = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request");
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), DEFAULT_STATUS, errorMsg), DEFAULT_STATUS);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionMessage> handlerConstraintViolationException(final ConstraintViolationException ex) {
        String errorMsg = ex.getConstraintViolations()
                .stream()
                .map(err -> err.getPropertyPath() + ": " + err.getMessage())
                .findFirst()
                .orElse("Invalid parameter");
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), DEFAULT_STATUS, errorMsg), DEFAULT_STATUS);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<ExceptionMessage> handleIllegalException(final Exception ex) {
        log.warn("Invalid argument: {}", ex.getMessage());
        String errorMsg = ex.getMessage();
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), DEFAULT_STATUS, errorMsg), DEFAULT_STATUS);
    }

    //  403 Error
    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<ExceptionMessage> handleAccessDeniedException(final Exception ex) {
        log.warn("Access denied: {}", ex.getMessage());
        String errorMsg = "You don't have permission to access this resource";
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), HttpStatus.FORBIDDEN, errorMsg), HttpStatus.FORBIDDEN);
    }

    //  429 Error
    @ExceptionHandler({RequestNotPermitted.class})
    public ResponseEntity<ExceptionMessage> handleRateLimitException(final Exception ex) {
        log.warn("Too many request: {}", ex.getMessage());
        String errorMsg = "Rate limit exceeded, please try again later";
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), HttpStatus.TOO_MANY_REQUESTS, errorMsg), HttpStatus.TOO_MANY_REQUESTS);
    }

    //  500 Error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionMessage> handleAllException(final Exception ex) {
        log.warn("Unhandled exception: {}", ex.getMessage());
        String errorMsg = "Internal server error";
        return new ResponseEntity<>(new ExceptionMessage(ZonedDateTime.now(ZoneId.systemDefault()), HttpStatus.INTERNAL_SERVER_ERROR, errorMsg), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
