package com.tresende.catalog.admin.application.genre.update;

import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateGenreUseCaseTest {

    @Mock
    GenreGateway genreGateway;

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(genreGateway);
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
        // given
        final var aGender = Genre.newGenre("acao", true);

        final var expectedId = aGender.getId();
        final var expectName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                UpdateGenreCommand.with(expectedId, expectName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.with(aGender)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(genreGateway, times(1)).findById(expectedId);

        verify(genreGateway, times(1)).update(argThat(aUpdatedGender ->
                Objects.equals(expectedId, aUpdatedGender.getId())
                        && Objects.equals(expectName, aUpdatedGender.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGender.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGender.getCategories())
                        && Objects.equals(aGender.getCreatedAt(), aUpdatedGender.getCreatedAt())
                        && aGender.getUpdatedAt().isBefore(aUpdatedGender.getUpdatedAt())
                        && Objects.isNull(aUpdatedGender.getDeletedAt())
        ));
    }

//    @Test
//    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
//        // given
//        final var expectName = "Ação";
//        final var expectedIsActive = true;
//        final var expectedCategories = List.of(
//                CategoryID.from("123"),
//                CategoryID.from("456")
//        );
//
//        final var aCommand =
//                UpdateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));
//
//        when(categoryGateway.existsByIds(any()))
//                .thenReturn(expectedCategories);
//
//        when(genreGateway.update(any()))
//                .thenAnswer(returnsFirstArg());
//
//        // when
//        final var actualOutput = useCase.execute(aCommand);
//
//        // then
//        Assertions.assertNotNull(actualOutput);
//        Assertions.assertNotNull(actualOutput.id());
//
//        Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);
//
//        Mockito.verify(genreGateway, times(1)).update(argThat(aGenre ->
//                Objects.equals(expectName, aGenre.getName())
//                        && Objects.equals(expectedIsActive, aGenre.isActive())
//                        && Objects.equals(expectedCategories, aGenre.getCategories())
//                        && Objects.nonNull(aGenre.getId())
//                        && Objects.nonNull(aGenre.getUpdatedAt())
//                        && Objects.nonNull(aGenre.getUpdatedAt())
//                        && Objects.isNull(aGenre.getDeletedAt())
//        ));
//    }
//
//    @Test
//    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
//        // given
//        final var expectName = "Ação";
//        final var expectedIsActive = false;
//        final var expectedCategories = List.<CategoryID>of();
//
//        final var aCommand =
//                UpdateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));
//
//        when(genreGateway.update(any()))
//                .thenAnswer(returnsFirstArg());
//
//        // when
//        final var actualOutput = useCase.execute(aCommand);
//
//        // then
//        Assertions.assertNotNull(actualOutput);
//        Assertions.assertNotNull(actualOutput.id());
//
//        Mockito.verify(genreGateway, times(1)).update(argThat(aGenre ->
//                Objects.equals(expectName, aGenre.getName())
//                        && Objects.equals(expectedIsActive, aGenre.isActive())
//                        && Objects.equals(expectedCategories, aGenre.getCategories())
//                        && Objects.nonNull(aGenre.getId())
//                        && Objects.nonNull(aGenre.getUpdatedAt())
//                        && Objects.nonNull(aGenre.getUpdatedAt())
//                        && Objects.nonNull(aGenre.getDeletedAt())
//        ));
//    }
//
//    @Test
//    public void givenAInvalidEmptyName_whenCallsUpdateGenre_shouldReturnDomainException() {
//        // given
//        final var expectName = " ";
//        final var expectedIsActive = true;
//        final var expectedCategories = List.<CategoryID>of();
//
//        final var expectedErrorMessage = "'name' should not be empty";
//        final var expectedErrorCount = 1;
//
//        final var aCommand =
//                UpdateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));
//
//        // when
//        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
//            useCase.execute(aCommand);
//        });
//
//        // then
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
//        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
//
//        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
//        Mockito.verify(genreGateway, times(0)).update(any());
//    }
//
//    @Test
//    public void givenAInvalidNullName_whenCallsUpdateGenre_shouldReturnDomainException() {
//        // given
//        final String expectName = null;
//        final var expectedIsActive = true;
//        final var expectedCategories = List.<CategoryID>of();
//
//        final var expectedErrorMessage = "'name' should not be null";
//        final var expectedErrorCount = 1;
//
//        final var aCommand =
//                UpdateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));
//
//        // when
//        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
//            useCase.execute(aCommand);
//        });
//
//        // then
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
//        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
//
//        Mockito.verify(categoryGateway, times(0)).existsByIds(any());
//        Mockito.verify(genreGateway, times(0)).update(any());
//    }
//
//    @Test
//    public void givenAValidCommand_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
//        // given
//        final var filmes = CategoryID.from("456");
//        final var series = CategoryID.from("123");
//        final var documentarios = CategoryID.from("789");
//
//        final var expectName = "Ação";
//        final var expectedIsActive = true;
//        final var expectedCategories = List.of(filmes, series, documentarios);
//
//        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
//        final var expectedErrorCount = 1;
//
//        when(categoryGateway.existsByIds(any()))
//                .thenReturn(List.of(series));
//
//        final var aCommand =
//                UpdateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));
//
//        // when
//        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
//            useCase.execute(aCommand);
//        });
//
//        // then
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
//        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
//
//        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
//        Mockito.verify(genreGateway, times(0)).update(any());
//    }
//
//    @Test
//    public void givenAInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
//        // given
//        final var filmes = CategoryID.from("456");
//        final var series = CategoryID.from("123");
//        final var documentarios = CategoryID.from("789");
//
//        final var expectName = " ";
//        final var expectedIsActive = true;
//        final var expectedCategories = List.of(filmes, series, documentarios);
//
//        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
//        final var expectedErrorMessageTwo = "'name' should not be empty";
//        final var expectedErrorCount = 2;
//
//        when(categoryGateway.existsByIds(any()))
//                .thenReturn(List.of(series));
//
//        final var aCommand =
//                UpdateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));
//
//        // when
//        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
//            useCase.execute(aCommand);
//        });
//
//        // then
//        Assertions.assertNotNull(actualException);
//        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
//        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
//        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
//
//        Mockito.verify(categoryGateway, times(1)).existsByIds(any());
//        Mockito.verify(genreGateway, times(0)).update(any());
//    }


    private List<String> asString(List<CategoryID> categories) {
        return categories.stream().map(CategoryID::getValue).toList();
    }

}