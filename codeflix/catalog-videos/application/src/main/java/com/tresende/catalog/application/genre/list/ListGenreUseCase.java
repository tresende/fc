package com.tresende.catalog.application.genre.list;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.genre.Genre;
import com.tresende.catalog.domain.genre.GenreGateway;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class ListGenreUseCase extends UseCase<ListGenreUseCase.Input, Pagination<ListGenreUseCase.Output>> {

    private final GenreGateway genreGateway;

    public ListGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<Output> execute(final ListGenreUseCase.Input input) {
        final var aQuery = new GenreSearchQuery(
                input.page(),
                input.perPage(),
                input.terms(),
                input.sort(),
                input.direction(),
                input.categories()
        );

        return this.genreGateway.findAll(aQuery)
                .map(Output::from);
    }

    public record Input(
            int page,
            int perPage,
            String terms,
            String sort,
            String direction,
            Set<String> categories
    ) {

    }

    public record Output(String id, String name, boolean active, Set<String> categories, Instant createdAt,
                         Instant updatedAt, Instant deletedAt) {

        public static Output from(Genre genre) {
            return new Output(
                    genre.id(),
                    genre.name(),
                    genre.active(),
                    genre.categories(),
                    genre.createdAt(),
                    genre.updatedAt(),
                    genre.deletedAt()
            );
        }
    }
}
