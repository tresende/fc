package com.tresende.catalog.application.genre.delete;

import com.tresende.catalog.application.UnitUseCase;
import com.tresende.catalog.domain.genre.GenreGateway;

import java.util.Objects;

public class DeleteGenreUseCase extends UnitUseCase<String> {

    private final GenreGateway genreGateway;

    public DeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String anId) {

        if (anId == null) {
            return;
        }

        genreGateway.deleteById(anId);
    }
}
