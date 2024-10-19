package com.tresende.catalog.admin.infrastructure.genre.persistence;

import com.tresende.catalog.admin.domain.category.CategoryID;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJpaEntity {

    @EmbeddedId
    private GenreCategoryID id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {
    }

    private GenreCategoryJpaEntity(final CategoryID aCategoryId, final GenreJpaEntity aGenre) {
        this.id = GenreCategoryID.from(aCategoryId.getValue(), aGenre.getId());
        this.genre = aGenre;
    }

    public static GenreCategoryJpaEntity from(
            final CategoryID aCategoryId, final GenreJpaEntity aGenre
    ) {
        return new GenreCategoryJpaEntity(aCategoryId, aGenre);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), genre);
    }

    public GenreCategoryID getId() {
        return id;
    }

    public void setId(final GenreCategoryID id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return genre;
    }

    public void setGenre(final GenreJpaEntity genre) {
        this.genre = genre;
    }
}
