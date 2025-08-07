package com.example.backend.exception.wrapper;

import java.io.Serial;

public class CinemaHallNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    public CinemaHallNotFoundException() {
	super();
    }

    public CinemaHallNotFoundException(String message, Throwable cause) {
	super(message, cause);
    }

    public CinemaHallNotFoundException(String message) {
	super(message);
    }

    public CinemaHallNotFoundException(Throwable cause) {
	super(cause);
    }

}
