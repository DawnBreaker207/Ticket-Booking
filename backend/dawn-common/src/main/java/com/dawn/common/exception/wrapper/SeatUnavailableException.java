package com.dawn.common.exception.wrapper;

import com.dawn.common.exception.ApiException;
import org.springframework.http.HttpStatus;

import java.io.Serial;

public class SeatUnavailableException extends ApiException {
    @Serial
    private static final long serialVersionUID = 1L;

    public SeatUnavailableException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
