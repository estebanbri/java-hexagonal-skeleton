package com.example.bfftienda.infrastructure.adapter.rest.exception;

public class UnexpectedException extends RuntimeException {
    static final long serialVersionUID = -2076457993124229948L;

    public UnexpectedException(String operationName) {
        this(operationName, null);
    }

    public UnexpectedException(String operationName, Throwable cause) {
        super(buildMessage(operationName, cause), cause);
    }

    private static String buildMessage(String operationName, Throwable cause) {
        return String.format("Error inesperado intentando %s. %s : %s", operationName, cause.getClass().getName(), cause.getMessage());
    }
}
