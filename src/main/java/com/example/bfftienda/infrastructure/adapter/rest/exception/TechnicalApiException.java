package com.example.bfftienda.infrastructure.adapter.rest.exception;

import org.springframework.http.HttpStatus;

import java.util.Objects;

public class TechnicalApiException extends ApiException {

    public TechnicalApiException(String message) {
        this(message, null);
    }

    public TechnicalApiException(String message, Throwable cause) {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "10001", concat(message,cause), cause);
    }

    private static String concat(String message, Throwable cause) {
      return Objects.nonNull(cause) ?  message + " Cause: " + cause.getMessage() : message;
    }
}
