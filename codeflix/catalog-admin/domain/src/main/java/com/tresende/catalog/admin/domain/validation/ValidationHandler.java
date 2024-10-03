package com.tresende.catalog.admin.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler aHandler);

    ValidationHandler validate(Validation aValidation);

    default boolean hasError() {
        return getErrors() != null && getErrors().isEmpty();
    }

    List<Error> getErrors();

    interface Validation {
        void validate();
    }
}
