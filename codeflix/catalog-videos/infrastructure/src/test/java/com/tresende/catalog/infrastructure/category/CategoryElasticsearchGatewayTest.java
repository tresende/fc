package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.AbstractElasticsearchTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CategoryElasticsearchGatewayTest extends AbstractElasticsearchTest {

    @Autowired
    CategoryElasticsearchGateway categoryGateway;

    @Autowired
    CategoryRepository categoryRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(categoryRepository);
        Assertions.assertNotNull(categoryGateway);
    }

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        //given
        final var aulas = Fixture.Categories.aulas();

        //when
        final var actualOutput = categoryGateway.save(aulas);

        //then
        Assertions.assertEquals(actualOutput, aulas);

        final var actualCategory = categoryRepository.findById(aulas.id()).get();
        Assertions.assertEquals(aulas.id(), actualCategory.id());
        Assertions.assertEquals(aulas.name(), actualCategory.name());
        Assertions.assertEquals(aulas.description(), actualCategory.description());
        Assertions.assertEquals(aulas.active(), actualCategory.active());
        Assertions.assertEquals(aulas.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(aulas.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aulas.deletedAt(), actualCategory.deletedAt());
    }
}
