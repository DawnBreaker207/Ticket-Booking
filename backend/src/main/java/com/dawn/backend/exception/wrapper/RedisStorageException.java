package com.dawn.backend.exception.wrapper;

import com.dawn.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class RedisStorageException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RedisStorageException(String message) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }
}
