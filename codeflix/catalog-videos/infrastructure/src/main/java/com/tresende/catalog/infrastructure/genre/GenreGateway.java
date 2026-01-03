package com.tresende.catalog.infrastructure.genre;

import com.tresende.catalog.infrastructure.genre.models.GenreDTO;

import java.util.Optional;

public interface GenreGateway {
    Optional<GenreDTO> genreOfId(String genreId);
}
