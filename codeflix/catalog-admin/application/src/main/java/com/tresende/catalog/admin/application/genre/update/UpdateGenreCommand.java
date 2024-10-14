package com.tresende.catalog.admin.application.genre.update;

import com.tresende.catalog.admin.domain.genre.GenreID;

import java.util.List;

public record UpdateGenreCommand(
        GenreID id,
        String name,
        boolean isActive,
        List<String> categories
) {

    public static UpdateGenreCommand with(
            final GenreID id,
            final String name,
            final Boolean isActive,
            final List<String> categories
    ) {
        final var safeIsActive = isActive != null ? isActive : false;
        return new UpdateGenreCommand(id, name, safeIsActive, categories);
    }
}
