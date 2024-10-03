package com.tresende.catalog.admin.domain.validation;

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
