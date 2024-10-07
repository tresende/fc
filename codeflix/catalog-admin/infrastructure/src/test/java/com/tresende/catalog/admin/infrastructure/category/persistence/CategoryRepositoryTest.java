package com.tresende.catalog.admin.infrastructure.category.persistence;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.infrastructure.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Filmes", "A Categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : com.tresende.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.name";

        anEntity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }


    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Filmes", "A Categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : com.tresende.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        anEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }


    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_shouldReturnError() {
        final var aCategory = Category.newCategory("Filmes", "A Categoria mais assistida", true);
        final var anEntity = CategoryJpaEntity.from(aCategory);
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : com.tresende.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        anEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());
        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}
