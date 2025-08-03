package com.example.backend.exception;

import java.io.Serial;

public class OrderNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public OrderNotFoundException() {
	super();
    }

    public OrderNotFoundException(String message, Throwable casue) {
	super(message, casue);
    }

    public OrderNotFoundException(String message) {
	super(message);
    }

    public OrderNotFoundException(Throwable casue) {
	super(casue);
    }
}
