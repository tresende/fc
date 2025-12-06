package com.tresende.catalog.infrastructure.graphql;

import com.tresende.catalog.GraphQLControllerTest;
import com.tresende.catalog.application.genre.list.ListGenreUseCase;
import com.tresende.catalog.application.genre.save.SaveGenreUseCase;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.domain.utils.IdUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = GenreGraphQLController.class)
class GenreGraphQLControllerTest {
    @MockitoBean
    private ListGenreUseCase listGenreUseCase;

    @MockitoBean
    private SaveGenreUseCase saveGenreUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListGenresShouldReturn() {
        //given
        final var expectedGenres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;
        final var expectedSearch = "";
        final var expectedCategories = Set.of();

        when(listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedItemsCount, expectedGenres));

        final var query = """
                {
                    genres{
                        id
                        name
                        categories
                        createdAt
                        updatedAt
                        deletedAt
                    }
                }
                """;

        final var res = graphql.document(query).execute();

        //when
        final var actualGenres = res.path("genres")
                .entityList(ListGenreUseCase.Output.class)
                .get();

        //then
        Assertions.assertTrue(
                actualGenres.size() == expectedGenres.size() &&
                        actualGenres.containsAll(expectedGenres)
        );

        final var capturer = ArgumentCaptor.forClass(GenreSearchQuery.class);
        verify(listGenreUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
        Assertions.assertEquals(expectedSearch, actualQuery.terms());
        Assertions.assertEquals(expectedCategories, actualQuery.categories());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListGenresShouldReturn() {
        //given
        final var expectedGenres = List.of(
                ListGenreUseCase.Output.from(Fixture.Genres.business()),
                ListGenreUseCase.Output.from(Fixture.Genres.tech())
        );

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedDirection = "desc";
        final var expectedSearch = "asd";
        final var expectedCategories = Set.of("c1");

        when(listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedGenres.size(), expectedGenres));

        final var query = """
                query allGenres($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String, $categories: [String!]) {
                    genres(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction, categories: $categories){
                        id
                        name
                        categories
                        createdAt
                        updatedAt
                        deletedAt
                    }
                }
                """;

        final var res = graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .variable("categories", expectedCategories)
                .execute();

        //when
        final var actualGenres = res.path("genres")
                .entityList(ListGenreUseCase.Output.class)
                .get();

        //then
        Assertions.assertTrue(
                actualGenres.size() == expectedGenres.size() &&
                        actualGenres.containsAll(expectedGenres)
        );

        final var capturer = ArgumentCaptor.forClass(GenreSearchQuery.class);
        verify(listGenreUseCase, times(1)).execute(capturer.capture());
    }

    @Test
    public void givenGenreInputWhenCallsSaveGenreMutationShouldPersistAndReturn() {
        //given

        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "Busines";
        final var expectedCategories = Set.of("c1", "c2");
        final var expectedActive = true;
        final var expectedDates = Instant.now();

        //when
        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "categories", expectedCategories,
                "active", expectedActive,
                "createdAt", expectedDates.toString(),
                "updatedAt", expectedDates.toString(),
                "deletedAt", expectedDates.toString()
        );

        final var query = """
                mutation SaveGenre($input: GenreInput!) {
                    genre: saveGenre(input: $input) {
                        id
                    }
                }
                """;

        doReturn(new SaveGenreUseCase.Output(expectedId)).when(saveGenreUseCase).execute(any());

        graphql.document(query)
                .variable("input", input)
                .execute()
                .path("genre.id").entity(String.class).isEqualTo(expectedId);

        //then

        final var capturer = ArgumentCaptor.forClass(SaveGenreUseCase.Input.class);
        verify(saveGenreUseCase, times(1)).execute(capturer.capture());

        final var actualGenre = capturer.getValue();
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(expectedActive, actualGenre.isActive());
        Assertions.assertEquals(expectedDates, actualGenre.createdAt());
        Assertions.assertEquals(expectedDates, actualGenre.updatedAt());
        Assertions.assertEquals(expectedDates, actualGenre.deletedAt());
    }
}
