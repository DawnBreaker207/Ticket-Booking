package com.dawn.common.core.exception.wrapper;

import com.dawn.common.core.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ResourceNotFoundException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}
