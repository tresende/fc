package com.tresende.catalog.infrastructure.exceptions;

import com.tresende.catalog.domain.exceptions.InternalErrorException;

public class NotFoundException extends InternalErrorException {

    protected NotFoundException(final String aMessage, final Throwable t) {
        super(aMessage, t);
    }

    public static NotFoundException with(final String message) {
        return new NotFoundException(message, null);
    }
}
