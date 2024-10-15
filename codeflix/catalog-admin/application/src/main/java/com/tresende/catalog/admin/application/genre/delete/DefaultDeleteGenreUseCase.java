package com.tresende.catalog.admin.application.genre.delete;

import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.genre.GenreID;

public class DefaultDeleteGenreUseCase extends DeleteGenreUseCase {

    private final GenreGateway genreGateway;

    public DefaultDeleteGenreUseCase(final GenreGateway genreGateway) {
        this.genreGateway = genreGateway;
    }

    @Override
    public void execute(final String anId) {
        genreGateway.deleteById(GenreID.from(anId));
    }
}
