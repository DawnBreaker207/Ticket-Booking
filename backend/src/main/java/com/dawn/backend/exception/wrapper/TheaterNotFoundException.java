package com.dawn.backend.exception.wrapper;

import com.dawn.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class TheaterNotFoundException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public TheaterNotFoundException(String message) {
        super(message);
    }

    public TheaterNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
