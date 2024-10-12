package com.tresende.catalog.admin.domain.genre;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {
    Category create(Genre aCategory);

    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    Genre update(Genre aGenre);

    Pagination<Genre> findAll(SearchQuery aQuery);
}
