package com.tresende.catalog.application.category.save;

import com.tresende.catalog.application.UseCaseTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.utils.IdUtils;
import com.tresende.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SaveCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Test
    public void givenValidCategory_whenCallsSave_shouldPersistIt() {
        //given
        final var aCategory = Fixture.Categories.aulas();

        when(categoryGateway.save(aCategory))
                .thenAnswer(returnsFirstArg());

        //when
        useCase.execute(aCategory);

        //then

        verify(categoryGateway, times(1)).save(any());
    }

    @Test
    public void givenInvalidCategory_whenCallsSave_shouldReturnError() {
        //given

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCategory = Category.with(
                IdUtils.uuid(),
                "",
                "Conteúdo Gravado",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        //when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCategory));

        //then
        assertEquals(expectedErrorMessage, actualError.getErrors().getFirst().message());
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        verify(categoryGateway, times(0)).save(any());
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        //given

        final var expectedErrorMessage = "'id' should not be empty";
        final var expectedErrorCount = 1;

        final var aCategory = Category.with(
                "",
                "Aulas",
                "Conteúdo Gravado",
                true,
                InstantUtils.now(),
                InstantUtils.now(),
                null
        );

        //when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCategory));

        //then
        assertEquals(expectedErrorMessage, actualError.getErrors().getFirst().message());
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        verify(categoryGateway, times(0)).save(any());
    }


    @Test
    public void givenNullCategory_whenCallsSave_shouldReturnError() {
        //given

        final var expectedErrorMessage = "category cant be null";
        final var expectedErrorCount = 1;

        final Category aCategory = null;

        //when
        final var actualError = Assertions.assertThrows(DomainException.class, () -> useCase.execute(aCategory));

        //then
        assertEquals(expectedErrorMessage, actualError.getErrors().getFirst().message());
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        verify(categoryGateway, times(0)).save(any());
    }
}
