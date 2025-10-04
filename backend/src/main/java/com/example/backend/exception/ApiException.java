package com.example.backend.exception;

import lombok.*;
import org.springframework.http.HttpStatus;


@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Data
@EqualsAndHashCode(callSuper = true)
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

}
