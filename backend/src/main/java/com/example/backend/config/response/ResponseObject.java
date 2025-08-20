package com.example.backend.config.response;

import com.fasterxml.jackson.annotation.JsonInclude;
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

    public static class Payload<T> {
        public int code;

        public String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        public T data;

        public Payload(int code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }
}
