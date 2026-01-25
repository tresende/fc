package com.tresende.catalog.infrastructure.category.models;

import com.tresende.catalog.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
class CategoryDTOTest {

    @Autowired
    private JacksonTester<CategoryDTO> json;

    @Test
    public void testUnmarshall_shouldReadSnakeCaseResponse() throws IOException {
        final var categoryResponse = """
                {
                    "id": "a26ce442a369459f9a1579abe6727efc",
                    "name": "Documentário",
                    "description": "A categoria de documentários",
                    "is_active": false,
                    "created_at": "2024-02-14T01:27:56.427186Z",
                    "updated_at": "2024-02-14T14:22:57.435831Z",
                    "deleted_at": "2024-02-14T14:22:57.435774Z"
                }
                """;

        final var actualCategory = this.json.parse(categoryResponse);

        Assertions.assertThat(actualCategory)
                .hasFieldOrPropertyWithValue("id", "a26ce442a369459f9a1579abe6727efc")
                .hasFieldOrPropertyWithValue("name", "Documentário")
                .hasFieldOrPropertyWithValue("description", "A categoria de documentários")
                .hasFieldOrPropertyWithValue("isActive", false)
                .hasFieldOrPropertyWithValue("createdAt", Instant.parse("2024-02-14T01:27:56.427186Z"))
                .hasFieldOrPropertyWithValue("updatedAt", Instant.parse("2024-02-14T14:22:57.435831Z"))
                .hasFieldOrPropertyWithValue("deletedAt", Instant.parse("2024-02-14T14:22:57.435774Z"));
    }
}