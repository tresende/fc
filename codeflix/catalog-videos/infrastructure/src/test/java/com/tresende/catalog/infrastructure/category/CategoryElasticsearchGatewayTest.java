package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.AbstractElasticsearchTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.infrastructure.category.persistence.CategoryDocument;
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

    @Test
    public void givenAValidId_whenCallsDeleteById_shouldDeleteIt() {
        //given
        final var aulas = Fixture.Categories.aulas();
        final var actualOutput = categoryRepository.save(CategoryDocument.from(aulas));

        final var expectedId = actualOutput.id();
        Assertions.assertTrue(categoryRepository.existsById(expectedId));

        //when
        categoryGateway.deleteById(expectedId);

        //then
        Assertions.assertFalse(categoryRepository.existsById(aulas.id()));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteById_shouldDeleteIt() {
        //given
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = aulas.id();
        Assertions.assertFalse(categoryRepository.existsById(expectedId));

        //when
        categoryGateway.deleteById(expectedId);

        //then
        Assertions.assertFalse(categoryRepository.existsById(aulas.id()));
    }

    @Test
    public void givenAValidId_whenCallsFindById_shouldRetrieveIt() {
        //given
        final var taks = Fixture.Categories.taks();
        final var actualOutput = categoryRepository.save(CategoryDocument.from(taks));

        final var expectedId = actualOutput.id();
        Assertions.assertTrue(categoryRepository.existsById(expectedId));

        //when
        final var actualCategory = categoryGateway.findById(expectedId).get();

        //then
        Assertions.assertEquals(taks.id(), actualCategory.id());
        Assertions.assertEquals(taks.name(), actualCategory.name());
        Assertions.assertEquals(taks.description(), actualCategory.description());
        Assertions.assertEquals(taks.active(), actualCategory.active());
        Assertions.assertEquals(taks.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(taks.updatedAt(), actualCategory.updatedAt());
    }


    @Test
    public void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        //given
        final var aulas = Fixture.Categories.aulas();

        final var expectedId = aulas.id();
        Assertions.assertFalse(categoryRepository.existsById(expectedId));

        //when
        final var actualCategory = categoryGateway.findById(expectedId);

        //then
        Assertions.assertTrue(actualCategory.isEmpty());
    }
}
