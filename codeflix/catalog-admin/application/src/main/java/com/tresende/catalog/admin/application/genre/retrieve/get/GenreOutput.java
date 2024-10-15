package com.tresende.catalog.admin.application.genre.retrieve.get;

import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static GenreOutput from(final Genre genre) {
        return new GenreOutput(
                genre.getId().getValue(),
                genre.getName(),
                genre.isActive(),
                genre.getCategories().stream().map(CategoryID::getValue).toList(),
                genre.getCreatedAt(),
                genre.getUpdatedAt(),
                genre.getDeletedAt()
        );
    }
}
