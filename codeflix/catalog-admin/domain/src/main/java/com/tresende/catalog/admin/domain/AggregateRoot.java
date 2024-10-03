package com.tresende.catalog.admin.domain;

import com.tresende.catalog.admin.domain.validation.ValidationHandler;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {
    public AggregateRoot(ID id) {
        super(id);
    }

    public void validate(ValidationHandler handler) {
        return;
    }
}
