package com.dawn.backend.exception.wrapper;

import com.dawn.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UserEmailNotFoundException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserEmailNotFoundException(String message) {
        super(message);
    }

    public UserEmailNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
