package com.example.backend.exception;

import java.io.Serial;

public class MovieExistedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public MovieExistedException() {
	super();
    }

    public MovieExistedException(String message, Throwable cause) {
	super(message, cause);
    }

    public MovieExistedException(String message) {
	super(message);
    }

    public MovieExistedException(Throwable cause) {
	super(cause);
    }
}
