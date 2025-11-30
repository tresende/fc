package com.tresende.catalog.application.genre.list;

import com.tresende.catalog.domain.genre.Genre;

public record ListGenreOutput(
        String id,
        String name
) {
    public static ListGenreOutput from(final Genre aGenre) {
        return new ListGenreOutput(aGenre.id(), aGenre.name());
    }
}
