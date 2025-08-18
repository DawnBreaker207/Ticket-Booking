package com.example.backend.exception.wrapper;

import java.io.Serial;

public class UserPasswordNotMatchException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserPasswordNotMatchException() {
    }

    public UserPasswordNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserPasswordNotMatchException(String message) {
        super(message);
    }

    public UserPasswordNotMatchException(Throwable cause) {
        super(cause);
    }
}
