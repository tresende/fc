package com.tresende.catalog.application.genre.save;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.genre.Genre;
import com.tresende.catalog.domain.genre.GenreGateway;
import com.tresende.catalog.domain.validation.Error;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;

public class SaveGenreUseCase extends UseCase<SaveGenreUseCase.Input, SaveGenreUseCase.Output> {

    private final GenreGateway genreGateway;

    public SaveGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Output execute(final Input input) {
        if (input == null) {
            throw DomainException.with(new Error("'SaveGenreUseCase.Input' cannot be null"));
        }

        final var aGenre = Genre.with(
                input.id,
                input.name,
                input.isActive,
                input.categories,
                input.createdAt,
                input.updatedAt,
                input.deletedAt
        );
        return new Output(this.genreGateway.save(aGenre));
    }

    public record Input(
            String id,
            String name,
            boolean isActive,
            Set<String> categories,
            Instant createdAt,
            Instant updatedAt,
            Instant deletedAt

    ) {
    }

    public record Output(
            String id
    ) {
        public Output(final Genre aGenre) {
            this(aGenre.id());
        }
    }
}
