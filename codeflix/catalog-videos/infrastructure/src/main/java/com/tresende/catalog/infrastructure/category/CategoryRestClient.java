package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.infrastructure.category.models.CategoryDTO;
import com.tresende.catalog.infrastructure.utils.HttpClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class CategoryRestClient implements HttpClient {

    public static final String NAMESPACE = "Category";
    private final RestClient restClient;

    public CategoryRestClient(
            final RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public Optional<CategoryDTO> getById(final String categoryId) {
        return doGet(categoryId, () ->
                restClient
                        .get()
                        .uri("/{id}", categoryId)
                        .retrieve()
                        .onStatus(is5xx, a5xxHandler(categoryId))
                        .onStatus(isNotFound, notFoundErrorHandler(categoryId))
                        .body(CategoryDTO.class)
        );
    }

    @Override
    public String namespace() {
        return NAMESPACE;
    }
}
