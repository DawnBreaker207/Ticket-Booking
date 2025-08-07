package com.example.backend.exception.wrapper;

import java.io.Serial;

public class OrderExpiredException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public OrderExpiredException() {

    }

    public OrderExpiredException(String message, Throwable cause) {
	super(message, cause);
    }

    public OrderExpiredException(String message) {
	super(message);
    }

    public OrderExpiredException(Throwable cause) {
	super(cause);
    }
}
