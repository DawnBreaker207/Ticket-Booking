package com.example.backend.exception.wrapper;

import java.io.Serial;

public class UsernameExistedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UsernameExistedException(Throwable cause) {
        super(cause);
    }

    public UsernameExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UsernameExistedException(String message) {
        super(message);
    }


}
