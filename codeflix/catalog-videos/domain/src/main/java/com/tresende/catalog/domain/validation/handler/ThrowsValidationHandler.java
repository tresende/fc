package com.tresende.catalog.domain.validation.handler;

import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(final Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler aHandler) {
        throw DomainException.with(aHandler.getErrors());
    }

    @Override
    public <T> T validate(Validation<T> aValidation) {
        try {
            aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(List.of(new Error(ex.getMessage())));
        }
        return null;
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}
