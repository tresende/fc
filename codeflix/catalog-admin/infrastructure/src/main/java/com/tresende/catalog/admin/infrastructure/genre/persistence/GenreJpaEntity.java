package com.tresende.catalog.admin.infrastructure.genre.persistence;

import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Entity
@Table(name = "genres")
public class GenreJpaEntity {
    @Column(name = "id", nullable = false)
    private String id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "active", nullable = false)
    private boolean active;
    @OneToMany(mappedBy = "genre", cascade = ALL, fetch = EAGER, orphanRemoval = true)
    private Set<GenreCategoryJpaEntity> categories;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;
    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public GenreJpaEntity() {
    }

    public GenreJpaEntity(
            final String anId,
            final String aName,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt) {
        this.id = anId;
        this.name = aName;
        this.active = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreJpaEntity from(final Genre aGenre) {
        final var anId = aGenre.getId().getValue();
        final var aName = aGenre.getName();
        final var isActive = aGenre.isActive();
        final var createdAt = aGenre.getCreatedAt();
        final var updatedAt = aGenre.getUpdatedAt();
        final var deletedAt = aGenre.getDeletedAt();
        final var anEntity = new GenreJpaEntity(anId, aName, isActive, createdAt, updatedAt, deletedAt);
        aGenre.getCategories().forEach(anEntity::addCategory);
        return anEntity;
    }

    public Genre toAggregate() {
        return Genre.with(
                GenreID.from(this.id),
                this.name,
                this.active,
                this.categories.stream()
                        .map(it -> CategoryID.from(it.getId().getCategoryId()))
                        .toList(),
                this.createdAt,
                this.updatedAt,
                this.deletedAt
        );
    }

    private void addCategory(final CategoryID anId) {
        final var aCategory = GenreCategoryJpaEntity.from(anId, this);
        this.categories.add(aCategory);
    }

    private void removeCategory(final CategoryID anId) {
        final var aCategory = GenreCategoryJpaEntity.from(anId, this);
        this.categories.add(aCategory);
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public void setCategories(final Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
