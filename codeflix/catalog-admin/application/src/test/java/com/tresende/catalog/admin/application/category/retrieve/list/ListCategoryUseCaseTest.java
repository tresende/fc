package com.tresende.catalog.admin.application.category.retrieve.list;

import com.tresende.catalog.admin.application.UseCaseTest;
import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;

public class ListCategoryUseCaseTest extends UseCaseTest {

    @Mock
    CategoryGateway categoryGateway;

    @InjectMocks
    private DefaultListCategoryUseCase useCase;

    @Override
    public List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAnValidQuery_whenCallsListCategory_thenShouldReturnCategories() {
        final var categories = List.of(
                Category.newCategory("filmes", null, true),
                Category.newCategory("series", null, true)
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                categories.size(),
                categories
        );

        final var expectedItemsCount = 2;
        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_wheHasNoResults_shouldReturnsEmptyCategories() {
        final List<Category> categories = Collections.emptyList();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedItemsCount = 0;

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedItemsCount,
                categories
        );

        final var expectedResult = expectedPagination.map(CategoryListOutput::from);

        Mockito.when(categoryGateway.findAll(eq(aQuery))).thenReturn(expectedPagination);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedItemsCount, actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedErrorMessage = "Gateway Error";


        Mockito.doThrow(new IllegalStateException(expectedErrorMessage)).when(categoryGateway).findAll(eq(aQuery));
        final var aCommand =
                Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, aCommand.getMessage());
    }
}
