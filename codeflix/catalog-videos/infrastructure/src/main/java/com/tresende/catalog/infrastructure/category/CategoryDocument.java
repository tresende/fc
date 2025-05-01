package com.tresende.catalog.infrastructure.category;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "categories")
public class CategoryDocument {
    @Id
    private String id;
}
