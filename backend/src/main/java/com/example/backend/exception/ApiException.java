package com.example.backend.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
public class ApiException extends RuntimeException {
    private final HttpStatus status;

    public ApiException(String message) {
        super(message);
        this.status = null;
    }

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
