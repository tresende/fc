package com.tresende.catalog.application.category.save;

import com.tresende.catalog.application.UseCaseTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class SaveCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        // given
        final var aCategory = Fixture.Categories.aulas();

        when(categoryGateway.save(any()))
                .thenAnswer(returnsFirstArg());

        // when
        this.useCase.execute(aCategory);

        // then
        verify(categoryGateway, times(1)).save(eq(aCategory));
    }

    @Test
    public void givenNullCategory_whenCallsSave_shouldReturnError() {
        // given
        final Category aCategory = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'aCategory' cannot be null";

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCategory));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(categoryGateway, times(0)).save(eq(aCategory));
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var aCategory = Category.with(
                "",
                "Aulas",
                "Conteudo gravado",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCategory));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(categoryGateway, times(0)).save(eq(aCategory));
    }

    @Test
    public void givenInvalidName_whenCallsSave_shouldReturnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCategory = Category.with(
                UUID.randomUUID().toString().replace("-", ""),
                "",
                "Conteudo gravado",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCategory));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(categoryGateway, times(0)).save(eq(aCategory));
    }
}
