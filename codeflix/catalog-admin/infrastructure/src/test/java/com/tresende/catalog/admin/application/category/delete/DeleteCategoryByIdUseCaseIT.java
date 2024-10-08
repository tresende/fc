package com.tresende.catalog.admin.application.category.delete;

import com.tresende.catalog.admin.IntegrationTest;
import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.tresende.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

@IntegrationTest
class DeleteCategoryByIdUseCaseIT {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    DeleteCategoryUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAnValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "Minha categoria", true);
        final var expectedId = aCategory.getId();
        save(aCategory);

        final var aCommand = DeleteCategoryCommand.with(expectedId.getValue());

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));

        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.unique();

        final var aCommand = DeleteCategoryCommand.with(expectedId.getValue());

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.unique();
        final var aCommand = DeleteCategoryCommand.with(expectedId.getValue());
        final var expectedMessage = "Gateway error";

        doThrow(new IllegalStateException(expectedMessage))
                .when(categoryGateway)
                .deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    private void save(final Category... aCategory) {
        final var categories = Arrays.stream(aCategory).map(CategoryJpaEntity::from).toList();
        categoryRepository.saveAllAndFlush(categories);
    }
}
