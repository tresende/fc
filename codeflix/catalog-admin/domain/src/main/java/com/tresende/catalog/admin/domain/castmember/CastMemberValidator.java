package com.tresende.catalog.admin.domain.castmember;

import com.tresende.catalog.admin.domain.validation.Error;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;
import com.tresende.catalog.admin.domain.validation.Validator;

class CastMemberValidator extends Validator {

    private final static int NAME_MIN_LENGTH = 3;
    private final static int NAME_MAX_LENGTH = 255;

    private final CastMember castMember;

    public CastMemberValidator(final CastMember aCastMember, final ValidationHandler handler) {
        super(handler);
        this.castMember = aCastMember;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        var name = this.castMember.getName();
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

    private void checkTypeConstraints() {
        var name = this.castMember.getType();
        if (name == null) {
            this.validationHandler().append(new Error("'type' should not be null"));
        }
    }
}
