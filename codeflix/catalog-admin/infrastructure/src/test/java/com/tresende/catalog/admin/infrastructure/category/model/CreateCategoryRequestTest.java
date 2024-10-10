package com.tresende.catalog.admin.infrastructure.category.model;

import com.tresende.catalog.admin.JacksonTest;
import com.tresende.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

@JacksonTest
class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    public void testMarshall() throws IOException {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var response = new CreateCategoryRequest(
                expectedId,
                expectedName,
                expectedIsActive
        );

        final var actualJson = json.write(response);
        Assertions.assertThat(actualJson)
                .hasJsonPath("$.name", expectedName)
                .hasJsonPath("$.description", expectedDescription)
                .hasJsonPath("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var json = """
                {
                  "name": "%s",
                  "description": "%s",
                  "is_active": %s
                }    
                """.formatted(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
        ;
    }
}
