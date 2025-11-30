package com.tresende.catalog.application.genre.list;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.genre.GenreGateway;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class ListGenreUseCase extends UseCase<GenreSearchQuery, Pagination<ListGenreOutput>> {

    private final GenreGateway genreGateway;

    public ListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<ListGenreOutput> execute(final GenreSearchQuery aQuery) {
        return genreGateway.findAll(aQuery).map(ListGenreOutput::from);
    }
}
