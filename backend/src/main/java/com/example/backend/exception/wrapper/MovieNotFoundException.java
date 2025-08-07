package com.example.backend.exception.wrapper;

import java.io.Serial;

public class MovieNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MovieNotFoundException() {
	super();
    }

    public MovieNotFoundException(String message, Throwable cause) {
	super(message, cause);
    }

    public MovieNotFoundException(String message) {
	super(message);
    }

    public MovieNotFoundException(Throwable cause) {
	super(cause);
    }
}
