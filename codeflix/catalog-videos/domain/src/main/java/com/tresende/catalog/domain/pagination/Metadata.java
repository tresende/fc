package com.tresende.catalog.domain.pagination;

public record Metadata(
        int currentPage,
        int perPage,
        long total
) {

}
