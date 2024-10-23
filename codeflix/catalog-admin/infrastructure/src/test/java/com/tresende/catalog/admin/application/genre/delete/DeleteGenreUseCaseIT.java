package com.tresende.catalog.admin.application.genre.delete;

import com.tresende.catalog.admin.IntegrationTest;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.genre.GenreID;
import com.tresende.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.Mockito.times;

@IntegrationTest
public class DeleteGenreUseCaseIT {

    @SpyBean
    GenreGateway genreGateway;

    @Autowired
    DeleteGenreUseCase useCase;

    @Autowired
    GenreRepository genreRepository;

    @Test
    public void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);
        Assertions.assertEquals(genreRepository.count(), 0);
        genreGateway.create(aGenre);

        final var expectedId = aGenre.getId();

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // when
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
        // given
        final var expectedId = GenreID.from("123");
        final var aGenre = Genre.newGenre("Ação", true);
        genreGateway.create(aGenre);
        Assertions.assertEquals(1, genreRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // when
        Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
        Assertions.assertEquals(1, genreRepository.count());
    }
}
