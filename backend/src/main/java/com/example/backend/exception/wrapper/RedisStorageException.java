package com.example.backend.exception.wrapper;

import java.io.Serial;

public class RedisStorageException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public RedisStorageException() {

    }

    public RedisStorageException(String message, Throwable cause) {
	super(message, cause);
    }

    public RedisStorageException(String message) {
	super(message);
    }

    public RedisStorageException(Throwable cause) {
	super(cause);
    }
}
