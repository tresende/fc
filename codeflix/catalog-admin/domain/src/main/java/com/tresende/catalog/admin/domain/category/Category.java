package com.tresende.catalog.admin.domain.category;

import com.tresende.catalog.admin.domain.AggregateRoot;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {
    private final Instant createdAt;
    private String description;
    private String name;
    private Boolean active;
    private Instant updatedAt;
    private Instant deletedAt;

    public Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final Boolean isActive,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        name = aName;
        description = aDescription;
        active = isActive;
        createdAt = aCreatedAt;
        updatedAt = anUpdatedAt;
        deletedAt = aDeletedAt;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean aIsActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedAt = aIsActive ? null : now;
        return new Category(id, aName, aDescription, aIsActive, now, now, deletedAt);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            deletedAt = Instant.now();
        }
        active = false;
        updatedAt = Instant.now();
        return this;
    }

    public Category activate() {
        deletedAt = null;
        active = true;
        updatedAt = Instant.now();
        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        name = aName;
        description = aDescription;
        active = isActive;
        return isActive ? activate() : deactivate();
    }
}