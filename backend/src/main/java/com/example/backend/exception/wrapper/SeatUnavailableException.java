package com.example.backend.exception.wrapper;

import java.io.Serial;

public class SeatUnavailableException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SeatUnavailableException() {
	super();
    }

    public SeatUnavailableException(String message, Throwable cause) {
	super(message, cause);
    }

    public SeatUnavailableException(String message) {
	super(message);
    }

    public SeatUnavailableException(Throwable cause) {
	super(cause);
    }

}
