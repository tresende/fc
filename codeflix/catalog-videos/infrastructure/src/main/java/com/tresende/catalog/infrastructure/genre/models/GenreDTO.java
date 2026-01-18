package com.tresende.catalog.infrastructure.genre.models;

import com.tresende.catalog.domain.genre.Genre;

import java.time.Instant;
import java.util.Set;

public record GenreDTO(
        String id,
        String name,
        Boolean isActive,
        Set<String> categoriesId,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public static GenreDTO from(final Genre aGenre) {
        return new GenreDTO(
                aGenre.id(),
                aGenre.name(),
                aGenre.active(),
                aGenre.categories(),
                aGenre.createdAt(),
                aGenre.updatedAt(),
                aGenre.deletedAt()
        );
    }
}
