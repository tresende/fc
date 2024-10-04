package com.tresende.catalog.admin.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler aHandler);

    ValidationHandler validate(Validation aValidation);

    default boolean hasError() {
        return getErrors() != null && !getErrors().isEmpty();
    }

    default Error firstError() {
        if (getErrors() == null || getErrors().isEmpty()) {
            return null;
        }
        return getErrors().getFirst();
    }

    List<Error> getErrors();

    interface Validation {
        void validate();
    }
}
