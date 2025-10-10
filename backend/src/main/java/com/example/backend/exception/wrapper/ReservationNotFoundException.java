package com.example.backend.exception.wrapper;

import com.example.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ReservationNotFoundException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public ReservationNotFoundException(String message) {
        super(message);
    }

    public ReservationNotFoundException(HttpStatus status, String message) {
        super(status, message);
    }
}
