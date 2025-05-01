package com.tresende.catalog.infrastructure.category;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CategoryElasticSearchRepository extends ElasticsearchRepository<CategoryDocument, String> {
}
