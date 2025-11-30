package com.tresende.catalog.application.genre.delete;

import com.tresende.catalog.application.UseCaseTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class DeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var aulas = Fixture.Categories.aulas();
        final var expectedId = aulas.id();

        doNothing().when(genreGateway)
                .deleteById(anyString());

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(genreGateway, never()).deleteById(any());
    }
}
