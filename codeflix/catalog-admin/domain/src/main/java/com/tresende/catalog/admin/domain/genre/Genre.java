package com.tresende.catalog.admin.domain.genre;

import com.tresende.catalog.admin.domain.AggregateRoot;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.exceptions.NotificationException;
import com.tresende.catalog.admin.domain.utils.InstantUtils;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;
import com.tresende.catalog.admin.domain.validation.handler.Notification;

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
        selfValidate();
    }

    public static Genre newGenre(final String name, final boolean isActive) {
        final var id = GenreID.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        final var emptyList = new ArrayList<CategoryID>();
        return new Genre(id, name, isActive, emptyList, now, now, deletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return with(
                aGenre.id,
                aGenre.name,
                aGenre.isActive,
                new ArrayList<>(aGenre.categories != null ? aGenre.categories : Collections.emptyList()),
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

    public void activate() {
        isActive = true;
        updatedAt = InstantUtils.now();
        deletedAt = null;
    }

    public void deactivate() {
        isActive = false;
        if (getDeletedAt() == null) {
            deletedAt = InstantUtils.now();
        }
        updatedAt = InstantUtils.now();
    }

    public Genre update(final String aName, final boolean isActive) {
        this.name = aName;
        this.isActive = isActive;
        if (isActive) {
            activate();
        } else {
            deactivate();
        }
        selfValidate();
        return this;
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {

        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());
        return update(aName, isActive);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("", notification);
        }
    }

    public Genre addCategory(final CategoryID aCategory) {
        if (aCategory == null) {
            return this;
        }

        categories.add(aCategory);
        updatedAt = InstantUtils.now();

        return this;
    }


    public Genre removeCategory(final CategoryID aCategory) {
        if (aCategory == null) {
            return this;
        }

        categories.remove(aCategory);
        updatedAt = InstantUtils.now();

        return this;
    }

    public Genre addCategories(final List<CategoryID> categories) {
        if (categories == null) return this;
        categories.forEach(this::addCategory);
        return this;
    }
}
