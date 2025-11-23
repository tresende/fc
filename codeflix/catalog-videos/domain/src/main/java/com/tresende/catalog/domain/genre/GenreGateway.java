package com.tresende.catalog.domain.genre;

import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface GenreGateway {

    Genre save(Genre aGenre);

    void deleteById(String anId);

    Optional<Genre> findById(String anId);

    Pagination<Genre> findAll(GenreSearchQuery aQuery);
}
