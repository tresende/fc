package com.tresende.catalog.graphql;

import com.tresende.catalog.GraphQLControllerTest;
import com.tresende.catalog.application.category.list.ListCategoryOutput;
import com.tresende.catalog.application.category.list.ListCategoryUseCase;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.category.CategorySearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.infrastructure.graphql.CategoryGraphQLController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = CategoryGraphQLController.class)
class CategoryGraphQLControllerTest {
    @MockBean
    private ListCategoryUseCase listCategoryUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListCategoriesShouldReturn() {
        //given
        final var expectedCategories = List.of(
                ListCategoryOutput.from(Fixture.Categories.lives()),
                ListCategoryOutput.from(Fixture.Categories.aulas())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        when(listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedItemsCount, expectedCategories));

        final var query = """
                {
                    categories{
                        id
                        name
                    }
                }
                """;

        final var res = graphql.document(query).execute();

        //when
        final var actualCategories = res.path("categories")
                .entityList(ListCategoryOutput.class)
                .get();

        //then
        Assertions.assertTrue(
                actualCategories.size() == expectedCategories.size() &&
                        actualCategories.containsAll(expectedCategories)
        );

        final var capturer = ArgumentCaptor.forClass(CategorySearchQuery.class);
        verify(listCategoryUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCategoriesShouldReturn() {
        //given
        final var categories = List.of(
                ListCategoryOutput.from(Fixture.Categories.lives()),
                ListCategoryOutput.from(Fixture.Categories.aulas())
        );

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedDirection = "desc";
        final var expectedSearch = "asd";

        when(listCategoryUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories));

        final var query = """
                query allCategories($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String) {
                    categories(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction){
                        id
                        name
                    }
                }
                """;

        final var res = graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .execute();

        //when
        final var actualCategories = res.path("categories")
                .entityList(ListCategoryOutput.class)
                .get();

        //then
        Assertions.assertTrue(
                actualCategories.size() == categories.size() &&
                        actualCategories.containsAll(categories)
        );

        final var capturer = ArgumentCaptor.forClass(CategorySearchQuery.class);
        verify(listCategoryUseCase, times(1)).execute(capturer.capture());

//        final var actualQuery = capturer.getValue();
//        Assertions.assertEquals(expectedPage, actualQuery.page());
//        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
//        Assertions.assertEquals(expectedSort, actualQuery.sort());
//        Assertions.assertEquals(expectedDirection, actualQuery.direction());
    }
}
