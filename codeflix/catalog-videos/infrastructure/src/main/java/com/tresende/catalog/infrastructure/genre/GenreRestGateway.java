package com.tresende.catalog.infrastructure.genre;

import com.tresende.catalog.infrastructure.authentication.GetClientCredentials;
import com.tresende.catalog.infrastructure.configuration.annotations.Genres;
import com.tresende.catalog.infrastructure.genre.models.GenreDTO;
import com.tresende.catalog.infrastructure.utils.HttpClient;
import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.apache.http.HttpHeaders;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Objects;
import java.util.Optional;

@Component
@CacheConfig(cacheNames = "admin-genres")
public class GenreRestGateway implements GenreGateway, HttpClient {

    public static final String GENRE = "genres";
    private final RestClient restClient;
    private final GetClientCredentials getClientCredentials;

    public GenreRestGateway(
            @Genres final RestClient restClient,
            final GetClientCredentials getClientCredentials) {
        this.restClient = Objects.requireNonNull(restClient);
        this.getClientCredentials = Objects.requireNonNull(getClientCredentials);
    }

    @Cacheable(key = "#genreId")
    @CircuitBreaker(name = GENRE)
    @Bulkhead(name = GENRE)
    @Retry(name = GENRE)
    public Optional<GenreDTO> genreOfId(final String genreId) {
        final var token = getClientCredentials.retrieve();
        return doGet(genreId, () ->
                restClient
                        .get()
                        .uri("/{id}", genreId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .onStatus(is5xx, a5xxHandler(genreId))
                        .onStatus(isNotFound, notFoundErrorHandler(genreId))
                        .body(GenreDTO.class)
        );
    }

    @Override
    public String namespace() {
        return GENRE;
    }
}
