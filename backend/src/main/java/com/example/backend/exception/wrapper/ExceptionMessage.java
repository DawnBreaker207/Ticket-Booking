package com.example.backend.exception.wrapper;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.ZonedDateTimeSerializer;

public class ExceptionMessage implements Serializable {
    @Serial
    private static final long serialVersioUID = 1L;

    @JsonSerialize(using = ZonedDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy__HH::mm:ss:SSSSSS")
    private final ZonedDateTime timeStamp;

    @JsonInclude(value = JsonInclude.Include.NON_NULL)
    private Throwable throwable;

    private final HttpStatus httpStatus;

    private final String message;

    public ExceptionMessage(ZonedDateTime timeStamp, HttpStatus httpStatus, String message) {
	this.timeStamp = timeStamp;
	this.httpStatus = httpStatus;
	this.message = message;
    }

    public ExceptionMessage(ZonedDateTime timestamp, Throwable throwable, HttpStatus httpStatus, String message) {
	this.timeStamp = timestamp;
	this.throwable = throwable;
	this.httpStatus = httpStatus;
	this.message = message;
    }

    public ZonedDateTime getTimestamp() {
	return timeStamp;
    }

    public Throwable getThrowable() {
	return throwable;
    }

    public void setThrowable(Throwable throwable) {
	this.throwable = throwable;
    }

    public HttpStatus getHttpStatus() {
	return httpStatus;
    }

    public String getMessage() {
	return message;
    }
}
