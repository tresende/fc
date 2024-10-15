package com.tresende.catalog.admin.application.genre.retrieve.list;

import com.tresende.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {

    public static GenreListOutput from(final Genre aCategory) {
        return new GenreListOutput(
                aCategory.getId().getValue(),
                aCategory.getName(),
                aCategory.isActive(),
                aCategory.getCategoriesAsStringList(),
                aCategory.getCreatedAt(),
                aCategory.getDeletedAt()
        );
    }
}
