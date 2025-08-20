package com.example.backend.exception.wrapper;

import com.example.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ForbiddenPermissionException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenPermissionException(String message) {
        super(message);
    }

    public ForbiddenPermissionException(HttpStatus status, String message) {
        super(status, message);
    }
}
