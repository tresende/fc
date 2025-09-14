package com.tresende.catalog.infrastructure.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tresende.catalog.AbstractRestClientTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.exceptions.InternalErrorException;
import com.tresende.catalog.infrastructure.category.models.CategoryDTO;
import io.github.resilience4j.bulkhead.BulkheadFullException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static io.github.resilience4j.circuitbreaker.CircuitBreaker.State.OPEN;


public class CategoryRestClientTest extends AbstractRestClientTest {
    public static final String CATEGORY = CategoryRestClient.NAMESPACE;

    @Autowired
    private CategoryRestClient target;

    @Test
    public void givenACategory_whenReceive200fromServer_shouldBeOk() {
        //given
        final var aulas = Fixture.Categories.aulas();
        final var responseBody = new CategoryDTO(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        );

        final var responseBodyJson = writeValueAsString(responseBody);

        stubFor(
                WireMock.get("/api/categories/%s".formatted(aulas.id()))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBodyJson))
        );

        //when

        final var actualCategory = target.getById(aulas.id()).get();

        //then
        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.active(), actualCategory.isActive());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenACategory_whenReceive5xxfromServer_shouldReturnInternalError() {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var expectedErrorMessage = "Error observed from Category [resourceId:%s] [status:500]".formatted(expectedId);
        final var responseBodyJson = writeValueAsString(Map.of("message", "Internal Server Error"));

        stubFor(
                WireMock.get("/api/categories/%s".formatted(expectedId))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBodyJson))
        );

        //when

        final var actualException = Assertions.assertThrows(
                InternalErrorException.class,
                () -> target.getById(expectedId)
        );

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }

    //404
    @Test
    public void givenACategory_whenReceive404fromServer_shouldReturnInternalError() throws JsonProcessingException {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var responseBodyJson = writeValueAsString(Map.of("message", "Not Found"));

        stubFor(
                WireMock.get("/api/categories/%s".formatted(expectedId))
                        .willReturn(aResponse()
                                .withStatus(404)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBodyJson))
        );

        //when

        final var actualCategory = target.getById(expectedId);

        //then
        Assertions.assertTrue(actualCategory.isEmpty());
        WireMock.verify(1, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }


    @Test
    public void givenACategory_whenReceiveTimeoutFromServer_shouldReturnInternalError() {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var expectedErrorMessage = "Timeout observed from Category [resourceId:%s]".formatted(expectedId);
        final var responseBodyJson = writeValueAsString(Map.of("message", "Internal Server Error"));

        stubFor(
                WireMock.get("/api/categories/%s".formatted(expectedId))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withFixedDelay(600)
                                .withBody(responseBodyJson))
        );

        //when

        final var actualException = Assertions.assertThrows(
                InternalErrorException.class,
                () -> target.getById(expectedId)
        );

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        WireMock.verify(2, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }

    @Test
    public void givenACategory_whenBulkheadIsfull_shouldReturnError() {
        //given
        final var expectedErrorMessage = "Bulkhead 'Category' is full and does not permit further calls";
        acquireBulkheadPermission(CATEGORY);

        //when
        final var actualException = Assertions.assertThrows(
                BulkheadFullException.class,
                () -> target.getById("123")
        );

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        releaseBulkheadPermission(CATEGORY);
    }

    @Test
    public void givenServerError_whenIsMoreThenThreshold_shouldOpenTheCircuitBreaker() {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var expectedErrorMessage = "Unhandled error observed from Category [resourceId:%s]".formatted(expectedId);
        final var responseBodyJson = writeValueAsString(Map.of("message", "Internal Server Error"));

        stubFor(
                WireMock.get("/api/categories/%s".formatted(expectedId))
                        .willReturn(aResponse()
                                .withStatus(500)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBodyJson))
        );

        // when
        Assertions.assertThrows(InternalErrorException.class, () -> target.getById(expectedId));
        Assertions.assertThrows(CallNotPermittedException.class, () -> target.getById(expectedId));

        // then
        assertCircuitBreakerState(CATEGORY, OPEN);
        WireMock.verify(3, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }

    @Test
    public void givenCall_whenCbIsOpen_shouldReturnError() {
        //given
        transitionToOpenState(CATEGORY);
        final var expectedId = Fixture.Categories.aulas().id();

        Assertions.assertThrows(CallNotPermittedException.class, () -> target.getById(expectedId));

        // then
        assertCircuitBreakerState(CATEGORY, OPEN);
        WireMock.verify(0, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(expectedId))));
    }

    @Test
    public void givenACategory_whenReceiveTwo_shouldReturnCachedValue() {
        //given
        final var aulas = Fixture.Categories.aulas();
        final var responseBody = new CategoryDTO(
                aulas.id(),
                aulas.name(),
                aulas.description(),
                aulas.active(),
                aulas.createdAt(),
                aulas.updatedAt(),
                aulas.deletedAt()
        );

        final var responseBodyJson = writeValueAsString(responseBody);

        stubFor(
                WireMock.get("/api/categories/%s".formatted(aulas.id()))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                .withBody(responseBodyJson))
        );

        //when

        target.getById(aulas.id()).get();
        target.getById(aulas.id()).get();
        final var actualCategory = target.getById(aulas.id()).get();

        //then
        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.active(), actualCategory.isActive());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());

        final var actualCachedValue = cache("admin-categories").get(aulas.id()).get();
        Assertions.assertEquals(actualCategory, actualCachedValue);
        verify(1, getRequestedFor(urlPathEqualTo("/api/categories/%s".formatted(aulas.id()))));
    }
}
