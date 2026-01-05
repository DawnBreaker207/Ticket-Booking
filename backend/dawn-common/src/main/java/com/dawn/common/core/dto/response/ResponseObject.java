package com.dawn.common.core.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

@Getter
public class ResponseObject<T> extends ResponseEntity<ResponseObject.Payload<T>> {

    public ResponseObject(
            @JsonProperty("code") int code,
            @JsonProperty("message") String message,
            @JsonProperty("data") T data
    ) {
        super(new Payload<>(code, message, data), HttpStatus.valueOf(code == 0 ? 200 : code));
    }

    public T getData() {
        return getBody() != null ? getBody().getData() : null;
    }

    public ResponseObject(HttpStatusCode code, String message, T data) {
        super(new Payload<>(code.value(), message, data), code);
    }

    public ResponseObject(HttpStatusCode code, String message, T data, HttpHeaders headers) {
        super(new Payload<>(code.value(), message, data), headers, code);
    }

    private ResponseObject(HttpStatusCode code) {
        super(code);
    }

    public static <T> ResponseObject<T> success(T data) {
        return new ResponseObject<>(HttpStatus.OK, "Success", data);
    }

    public static <T> ResponseObject<T> success(T data, String message) {
        return new ResponseObject<>(HttpStatus.OK, message, data);
    }

    public static <T> ResponseObject<T> created(T data) {
        return new ResponseObject<>(HttpStatus.CREATED, "Created Successfully", data);
    }

    public static <T> ResponseObject<T> deleted() {
        return new ResponseObject<>(HttpStatus.NO_CONTENT);
    }

    public static <T> ResponseObject<T> error(HttpStatus code, String message) {
        return new ResponseObject<>(code, message, null);
    }

    @Value
    public static class Payload<T> {
        int code;

        String message;

        @JsonInclude(JsonInclude.Include.NON_NULL)
        T data;
    }
}
