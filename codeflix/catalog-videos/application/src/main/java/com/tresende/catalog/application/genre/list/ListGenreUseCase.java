package com.tresende.catalog.application.genre.list;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.genre.Genre;
import com.tresende.catalog.domain.genre.GenreGateway;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Objects;
import java.util.Set;

public class ListGenreUseCase extends UseCase<GenreSearchQuery, Pagination<ListGenreUseCase.Output>> {

    private final GenreGateway genreGateway;

    public ListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<Output> execute(final GenreSearchQuery aQuery) {
        return genreGateway.findAll(aQuery).map(Output::from);

    }

    public record Output(
            String id,
            String name,
            Set<String> categories
    ) {
        public static Output from(Genre aGenre) {
            return new Output(aGenre.id(), aGenre.name(), aGenre.categories());
        }
    }
}
