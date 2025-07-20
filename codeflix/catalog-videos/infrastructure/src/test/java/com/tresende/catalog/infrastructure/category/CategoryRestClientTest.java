package com.tresende.catalog.infrastructure.category;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.tresende.catalog.IntegrationTestConfiguration;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.exceptions.InternalErrorException;
import com.tresende.catalog.infrastructure.category.models.CategoryDTO;
import com.tresende.catalog.infrastructure.configuration.WebServerConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;

@ActiveProfiles("test-integration")
@EnableAutoConfiguration(exclude = {
        KafkaAutoConfiguration.class,
        ElasticsearchRepositoriesAutoConfiguration.class
})
@SpringBootTest(classes = {
        WebServerConfig.class,
        IntegrationTestConfiguration.class
})
@AutoConfigureWireMock(port = 0)
public class CategoryRestClientTest {
    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private CategoryRestClient target;

    @Test
    public void givenACategory_whenReceive200fromServer_shouldBeOk() throws JsonProcessingException {
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

        final var responseBodyJson = objectMapper.writeValueAsString(responseBody);

        WireMock.stubFor(
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
    public void givenACategory_whenReceive5xxfromServer_shouldReturnInternalError() throws JsonProcessingException {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var expectedErrorMessage = "Failed to get Category of id %s".formatted(expectedId);
        final var responseBodyJson = objectMapper.writeValueAsString(Map.of("message", "Internal Server Error"));

        WireMock.stubFor(
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
    }

    //404
    @Test
    public void givenACategory_whenReceive404fromServer_shouldReturnInternalError() throws JsonProcessingException {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var responseBodyJson = objectMapper.writeValueAsString(Map.of("message", "Not Found"));

        WireMock.stubFor(
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
    }


    @Test
    public void givenACategory_whenReceiveTimeoutFromServer_shouldReturnInternalError() throws JsonProcessingException {
        //given
        final var expectedId = Fixture.Categories.aulas().id();
        final var expectedErrorMessage = "Timeout on get Category of id %s".formatted(expectedId);
        final var responseBodyJson = objectMapper.writeValueAsString(Map.of("message", "Internal Server Error"));

        WireMock.stubFor(
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
    }
}
