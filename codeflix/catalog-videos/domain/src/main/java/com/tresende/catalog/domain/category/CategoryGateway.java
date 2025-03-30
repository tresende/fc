package com.tresende.catalog.domain.category;

import com.tresende.catalog.domain.pagination.Pagination;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category save(Category aCategory);

    void deleteById(String anId);

    Optional<Category> findById(String anId);

    List<String> existsByIds(Iterable<String> ids);

    Pagination<Category> findAll(CategorySearchQuery aQuery);
}
