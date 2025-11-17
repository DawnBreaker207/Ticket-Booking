package com.dawn.backend.exception.wrapper;

import com.dawn.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class SeatUnavailableException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SeatUnavailableException(String message) {
        super(message);
    }

    public SeatUnavailableException(HttpStatus status, String message) {
        super(status, message);
    }
}
