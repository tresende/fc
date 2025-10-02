package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.infrastructure.category.models.CategoryDTO;
import com.tresende.catalog.infrastructure.utils.HttpClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-categories")
public class CategoryRestGateway implements CategoryGateway, HttpClient {

    public static final String CATEGORY = "categories";
    private final RestClient restClient;

    public CategoryRestGateway(
            final RestClient restClient
    ) {
        this.restClient = restClient;
    }


    @Cacheable(key = "#categoryId")
    @CircuitBreaker(name = CATEGORY)
    @Bulkhead(name = CATEGORY)
    @Retry(name = CATEGORY)
    public Optional<Category> categoryOfId(final String categoryId) {
        return doGet(categoryId, () ->
                restClient
                        .get()
                        .uri("/{id}", categoryId)
                        .retrieve()
                        .onStatus(is5xx, a5xxHandler(categoryId))
                        .onStatus(isNotFound, notFoundErrorHandler(categoryId))
                        .body(CategoryDTO.class)
        ).map(CategoryDTO::toCategory);
    }

    @Override
    public String namespace() {
        return CATEGORY;
    }
}
