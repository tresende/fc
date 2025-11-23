package com.tresende.catalog.domain.genre;

public record GenreSearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
