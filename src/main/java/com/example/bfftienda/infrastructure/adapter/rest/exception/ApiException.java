package com.example.bfftienda.infrastructure.adapter.rest.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String errorCode;

    public ApiException(HttpStatus httpStatus, String errorCode, String message) {
        this(httpStatus, errorCode, message, null);
    }

    public ApiException(HttpStatus httpStatus, String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
    }

    public HttpStatus httpStatus() {
        return httpStatus;
    }

    public String errorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return super.toString() + " httpStatus:'" + httpStatus + "' errorCode:'" + errorCode + "'";
    }
}
