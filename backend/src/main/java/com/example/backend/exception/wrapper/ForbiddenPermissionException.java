package com.example.backend.exception.wrapper;

import java.io.Serial;

public class ForbiddenPermissionException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ForbiddenPermissionException() {

    }

    public ForbiddenPermissionException(String message, Throwable cause) {
	super(message, cause);
    }

    public ForbiddenPermissionException(String message) {
	super(message);
    }

    public ForbiddenPermissionException(Throwable cause) {
	super(cause);
    }
}
