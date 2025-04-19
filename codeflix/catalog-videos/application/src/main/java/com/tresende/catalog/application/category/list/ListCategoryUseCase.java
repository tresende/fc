package com.tresende.catalog.application.category.list;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.category.CategorySearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<ListCategoryOutput>> {

    private final CategoryGateway categoryGateway;

    public ListCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<ListCategoryOutput> execute(final CategorySearchQuery aQuery) {
        return categoryGateway.findAll(aQuery).map(ListCategoryOutput::from);
    }
}
