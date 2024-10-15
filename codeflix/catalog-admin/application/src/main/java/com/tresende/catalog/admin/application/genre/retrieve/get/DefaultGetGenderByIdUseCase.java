package com.tresende.catalog.admin.application.genre.retrieve.get;

import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.genre.GenreID;

import java.util.Objects;

public class DefaultGetGenderByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public DefaultGetGenderByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anIn) {
        final var aGenreId = GenreID.from(anIn);
        return this.genreGateway.findById(aGenreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }
}
