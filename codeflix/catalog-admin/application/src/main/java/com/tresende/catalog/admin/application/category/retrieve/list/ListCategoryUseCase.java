package com.tresende.catalog.admin.application.category.retrieve.list;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.Pagination;
import com.tresende.catalog.admin.domain.category.CategorySearchQuery;

public abstract class ListCategoryUseCase extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}