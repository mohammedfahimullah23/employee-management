package com.example.learningjava.exceptions;

public class AzureFunctionException extends RuntimeException {
    public AzureFunctionException(String message, Throwable cause) {
        super(message, cause);
    }
}

