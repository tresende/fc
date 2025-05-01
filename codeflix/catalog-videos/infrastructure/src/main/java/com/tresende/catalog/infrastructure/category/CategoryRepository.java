package com.tresende.catalog.infrastructure.category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryRepository extends ElasticsearchRepository<CategoryDocument, String> {
}
