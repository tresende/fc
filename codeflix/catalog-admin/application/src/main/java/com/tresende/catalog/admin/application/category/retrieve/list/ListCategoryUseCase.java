package com.tresende.catalog.admin.application.category.retrieve.list;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;

public abstract class ListCategoryUseCase extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}