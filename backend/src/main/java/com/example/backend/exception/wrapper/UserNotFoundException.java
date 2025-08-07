package com.example.backend.exception.wrapper;

import java.io.Serial;

public class UserNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserNotFoundException() {
	super();
    }

    public UserNotFoundException(String message, Throwable cause) {
	super(message, cause);
    }

    public UserNotFoundException(String message) {
	super(message);
    }

    public UserNotFoundException(Throwable cause) {
	super(cause);
    }

}
