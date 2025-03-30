package com.tresende.catalog.domain.category;

import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class Category implements Cloneable {
    private final Instant createdAt;
    private final String id;
    private final String description;
    private final String name;
    private final Boolean active;
    private final Instant updatedAt;
    private final Instant deletedAt;

    public Category(
            final String anId,
            final String aName,
            final String aDescription,
            final Boolean isActive,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        this.id = anId;
        name = aName;
        description = aDescription;
        active = isActive;
        createdAt = aCreatedAt;
        updatedAt = anUpdatedAt;
        deletedAt = aDeletedAt;
    }

    public static Category with(
            final String anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.id(),
                aCategory.name,
                aCategory.description,
                aCategory.active(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    public String id() {
        return id;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public String description() {
        return description;
    }

    public String name() {
        return name;
    }

    public Boolean active() {
        return active;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    public Category validate(ValidationHandler aHandler) {
        if (id == null || id.isBlank()) {
            aHandler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            aHandler.append(new Error("'name' should not be empty"));
        }
        return this;
    }
}