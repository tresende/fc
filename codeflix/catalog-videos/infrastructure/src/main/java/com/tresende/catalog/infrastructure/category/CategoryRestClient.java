package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.domain.exceptions.InternalErrorException;
import com.tresende.catalog.infrastructure.category.models.CategoryDTO;
import com.tresende.catalog.infrastructure.exceptions.NotFoundException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.net.http.HttpTimeoutException;
import java.util.Optional;
import java.util.concurrent.TimeoutException;

@Service
public class CategoryRestClient {

    private final RestClient restClient;

    public CategoryRestClient(
            final RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public Optional<CategoryDTO> getById(final String categoryId) {
        try {
            final var response = restClient
                    .get()
                    .uri("/{id}", categoryId)
                    .retrieve()
                    .onStatus(HttpStatusCode::is5xxServerError,
                            (req, res) -> {
                                throw InternalErrorException.with("Failed to get Category of id %s".formatted(categoryId));
                            }
                    ).onStatus(HttpStatusCode::is4xxClientError, (req, res) -> {
                                throw NotFoundException.with("Category of id %s was not found".formatted(categoryId));
                            }
                    )
                    .body(CategoryDTO.class);
            return Optional.ofNullable(response);
        } catch (NotFoundException ex) {
            return Optional.empty();
        } catch (ResourceAccessException ex) {
            final var cause = ExceptionUtils.getRootCause(ex);
            if (cause instanceof HttpTimeoutException || cause instanceof TimeoutException) {
                throw InternalErrorException.with("Timeout on get Category of id %s".formatted(categoryId));
            }
            throw ex;
        }
    }
}
