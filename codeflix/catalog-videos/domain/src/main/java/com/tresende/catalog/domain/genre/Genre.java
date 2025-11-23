package com.tresende.catalog.domain.genre;

import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.ValidationHandler;
import com.tresende.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public record Genre(String id, String name, Set<String> categories, boolean active, Instant createdAt,
                    Instant updatedAt, Instant deletedAt) {
    public Genre(
            final String id,
            final String name,
            final Set<String> categories,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.categories = categories == null ? Collections.emptySet() : new HashSet<>(categories);
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        validate(new ThrowsValidationHandler());
    }

    public static Genre with(
            final String anId,
            final String name,
            final boolean active,
            final Set<String> categories,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Genre(
                anId,
                name,
                categories,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.categories,
                aGenre.active,
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public Genre validate(ValidationHandler aHandler) {
        if (id == null || id.isBlank()) {
            aHandler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            aHandler.append(new Error("'name' should not be empty"));
        }

        return this;
    }
}
