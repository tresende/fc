package com.tresende.catalog.admin.application.genre.retrieve.get;

import com.tresende.catalog.admin.IntegrationTest;
import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class GetGenreByIdUseCaseIT {

    @Autowired
    private DefaultGetGenreByIdUseCase useCase;

    @SpyBean
    private GenreGateway genreGateway;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetGenre_ShouldReturnGenre() {
        //given
        final var expectedName = "Ação";
        final var expectedIsActive = true;

        final var filmes = categoryGateway.create(Category.newCategory("Filmes", "", true));
        final var series = categoryGateway.create(Category.newCategory("Series", "", true));

        final var expectedCategories = List.of(
                filmes.getId(),
                series.getId()
        );

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();
        aGenre.addCategories(expectedCategories);
        genreGateway.update(aGenre);

        //when
        final var actualGenre = useCase.execute(expectedId.getValue());

        //then

        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size() &&
                        asString(expectedCategories).containsAll(actualGenre.categories())
        );
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

        //when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        //then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(genreGateway, times(1)).findById(expectedId);
    }

    private List<String> asString(List<CategoryID> ids) {
        return ids.stream().map(CategoryID::getValue).toList();
    }
}
