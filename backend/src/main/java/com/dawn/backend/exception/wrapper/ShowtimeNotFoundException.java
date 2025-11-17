package com.dawn.backend.exception.wrapper;

import com.dawn.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ShowtimeNotFoundException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ShowtimeNotFoundException(String message) {
        super(message);
    }

    public ShowtimeNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
