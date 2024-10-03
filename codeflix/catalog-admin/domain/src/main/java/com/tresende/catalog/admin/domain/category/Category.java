package com.tresende.catalog.admin.domain.category;

import com.tresende.catalog.admin.domain.AggregateRoot;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryID> {
    private final String name;
    private final String description;
    private final String active;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;

    public Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final String isActive,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.deletedAt = aDeletedAt;
    }

    public static Category newCategory(final String aName, final String aDescription, final String aIsActive) {
        final var id = CategoryID.unique();
        final var now = Instant.now();
        return new Category(id, aName, aDescription, aIsActive, now, now, null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIsActive() {
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
}