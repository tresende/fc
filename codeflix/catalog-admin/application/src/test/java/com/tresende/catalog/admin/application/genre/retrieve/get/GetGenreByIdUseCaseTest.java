package com.tresende.catalog.admin.application.genre.retrieve.get;

import com.tresende.catalog.admin.application.UseCaseTest;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    public List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenre_ShouldReturnGenre() {
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();
        aGenre.addCategories(expectedCategories);

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(aGenre));

        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(aGenre.getId());
    }


    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_ShouldReturnNotFound() {
        //given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(genreGateway.findById(eq(expectedId)))
                .thenReturn(Optional.empty());

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1)).findById(expectedId);
    }
}
