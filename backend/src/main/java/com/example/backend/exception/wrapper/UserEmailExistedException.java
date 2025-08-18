package com.example.backend.exception.wrapper;

import java.io.Serial;

public class UserEmailExistedException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public UserEmailExistedException() {
    }

    public UserEmailExistedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserEmailExistedException(String message) {
        super(message);
    }

    public UserEmailExistedException(Throwable cause) {
        super(cause);
    }


}
