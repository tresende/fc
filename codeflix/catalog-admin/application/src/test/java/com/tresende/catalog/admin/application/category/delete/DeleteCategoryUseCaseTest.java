package com.tresende.catalog.admin.application.category.delete;

import com.tresende.catalog.admin.application.UseCaseTest;
import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

class DeleteCategoryUseCaseTest extends UseCaseTest {

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Override
    public List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAnValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var aCategory = Category.newCategory("Filmes", "Minha categoria", true);
        final var expectedId = aCategory.getId();

        final var aCommand = DeleteCategoryCommand.with(expectedId.getValue());
        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnInValidId_whenCallsDeleteCategory_shouldBeOk() {
        final var expectedId = CategoryID.unique();

        final var aCommand = DeleteCategoryCommand.with(expectedId.getValue());
        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCommand));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    public void givenAnValidId_whenGatewayThrowsException_shouldReturnException() {
        final var expectedId = CategoryID.unique();
        final var aCommand = DeleteCategoryCommand.with(expectedId.getValue());
        final var expectedMessage = "Gateway error";

        Mockito.doThrow(new IllegalStateException(expectedMessage)).when(categoryGateway).deleteById(eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aCommand));
        Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }
}