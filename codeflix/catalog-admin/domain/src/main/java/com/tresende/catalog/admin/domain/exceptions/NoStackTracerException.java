package com.tresende.catalog.admin.domain.exceptions;

public class NoStackTracerException extends RuntimeException {

    public NoStackTracerException(String message) {
        this(message, null);
    }

    public NoStackTracerException(String message, Throwable cause) {
        super(message, cause, true, false);
    }
}
