package com.tresende.catalog.domain.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    public Validator(ValidationHandler handler) {
        this.handler = handler;
    }

    protected ValidationHandler validationHandler() {
        return this.handler;
    }

    public abstract void validate();
}
