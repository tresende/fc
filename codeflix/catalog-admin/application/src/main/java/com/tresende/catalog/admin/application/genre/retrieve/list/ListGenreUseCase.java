package com.tresende.catalog.admin.application.genre.retrieve.list;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;

public abstract class ListGenreUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {
}