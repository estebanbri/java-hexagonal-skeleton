package com.example.bfftienda.infrastructure.adapter.rest.exception;

import org.springframework.http.HttpStatus;

public class BusinessApiException extends ApiException {
    private static final long serialVersionUID = -287269408660273263L;
    private static final String ERROR_CODE = "00002";

    public BusinessApiException(HttpStatus httpStatus, String message) {
        super(httpStatus, ERROR_CODE, message);
    }

    public BusinessApiException(HttpStatus httpStatus, String errorCode, String message) {
        super(httpStatus, errorCode, message);
    }

}

