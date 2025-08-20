package com.example.backend.exception.wrapper;

import com.example.backend.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class OrderExpiredException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;


    public OrderExpiredException(String message) {
        super(message);
    }

    public OrderExpiredException(HttpStatus status, String message) {
        super(status, message);
    }
}
