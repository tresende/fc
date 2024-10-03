package com.tresende.catalog.admin.domain.exceptions;

import com.tresende.catalog.admin.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class DomainException extends NoStackTracerException {

    private final List<Error> errors;

    private DomainException(final String aMessage, final List<Error> anErrors) {
        super(aMessage);
        this.errors = anErrors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), Collections.singletonList(anError));
    }

    public static DomainException with(final List<Error> anErrors) {
        return new DomainException("", anErrors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
