package com.dawn.backend.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseObject<T> extends ResponseEntity<ResponseObject.Payload<T>> {

    public ResponseObject(HttpStatusCode code, String message, T data) {
        super(new Payload<>(code.value(), message, data), code);
    }

    public ResponseObject(HttpStatusCode code, String message, T data, HttpHeaders headers) {
        super(new Payload<>(code.value(), message, data), headers, code);
    }

    @Value
    public static class Payload<T> {
        int code;

        String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data;
    }
}
