package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.domain.category.Category;

import java.util.Optional;

public interface CategoryGateway {

    Optional<Category> categoryOfId(String id);
}
