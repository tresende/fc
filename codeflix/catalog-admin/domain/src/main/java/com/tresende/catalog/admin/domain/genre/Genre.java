package com.tresende.catalog.admin.domain.genre;

import com.tresende.catalog.admin.domain.AggregateRoot;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean isActive;
    private List<CategoryID> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    protected Genre(final GenreID genreID) {
        super(genreID);
    }

    private Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt) {
        super(anId);
        this.name = aName;
        this.isActive = isActive;
        this.categories = categories;
        createdAt = Objects.requireNonNull(aCreatedAt, "'createdAt' should not be null");
        updatedAt = Objects.requireNonNull(anUpdatedAt, "'updatedAt' should not be null");
        deletedAt = aDeletedAt;
    }

    public static Genre newGenre(String name, boolean aIsActive) {
        final var id = GenreID.unique();
        final var now = Instant.now();
        final var deletedAt = aIsActive ? null : now;
        final var emptyList = new ArrayList<CategoryID>();
        return new Genre(id, name, aIsActive, emptyList, now, now, deletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return with(
                aGenre.id,
                aGenre.name,
                aGenre.isActive,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt) {

        return new Genre(
                anId,
                aName,
                isActive,
                categories,
                aCreatedAt,
                anUpdatedAt,
                aDeletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return isActive;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(categories);
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
