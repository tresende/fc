package com.tresende.catalog.admin.application.genre.create;

import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreID;

public record CreateGenreOutput(
        String id
) {

    public static CreateGenreOutput from(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }

    public static CreateGenreOutput from(final GenreID anId) {
        return new CreateGenreOutput(anId.getValue());
    }

    public static CreateGenreOutput from(final String anId) {
        return new CreateGenreOutput(anId);
    }
}
