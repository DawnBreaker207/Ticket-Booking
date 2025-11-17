package com.dawn.backend.exception.wrapper;

import com.dawn.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class RefreshTokenExpiredException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RefreshTokenExpiredException(String message) {
        super(message);
    }

    public RefreshTokenExpiredException(HttpStatus status, String message) {
        super(status, message);
    }
}
