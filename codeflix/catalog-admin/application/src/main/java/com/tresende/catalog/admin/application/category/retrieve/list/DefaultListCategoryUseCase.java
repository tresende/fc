package com.tresende.catalog.admin.application.category.retrieve.list;

import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListCategoryUseCase extends ListCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultListCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(SearchQuery aQuery) {
        return categoryGateway.findAll(aQuery).map(CategoryListOutput::from);
    }
}
