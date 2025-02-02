package com.tresende.catalog.admin.domain;

import com.tresende.catalog.admin.domain.event.DomainEvent;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;

import java.util.Collections;
import java.util.List;

public class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    public AggregateRoot(final ID id) {
        this(id, Collections.emptyList());
    }

    public AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }

    public void validate(ValidationHandler handler) {
        return;
    }
}
