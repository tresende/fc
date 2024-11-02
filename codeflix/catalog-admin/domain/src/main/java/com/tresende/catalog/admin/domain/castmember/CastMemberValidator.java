package com.tresende.catalog.admin.domain.castmember;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.validation.Error;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;
import com.tresende.catalog.admin.domain.validation.Validator;

class CastMemberValidator extends Validator {

    private final static int NAME_MIN_LENGTH = 3;
    private final static int NAME_MAX_LENGTH = 255;

    private final Category category;

    public CastMemberValidator(final Category aCategory, final ValidationHandler handler) {
        super(handler);
        this.category = aCategory;
    }

    @Override
    public void validate() {
        checkNameConstraints();
    }

    private void checkNameConstraints() {
        var name = this.category.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }
        name = name.trim();
        if (name.isEmpty()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }
        if (name.length() < NAME_MIN_LENGTH || name.length() > NAME_MAX_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}
