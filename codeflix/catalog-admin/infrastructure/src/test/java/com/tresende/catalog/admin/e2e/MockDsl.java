package com.tresende.catalog.admin.e2e;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.genre.GenreID;
import com.tresende.catalog.admin.infrastructure.category.models.CategoryResponse;
import com.tresende.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import com.tresende.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.tresende.catalog.admin.infrastructure.configuration.json.Json;
import com.tresende.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public interface MockDsl {
    MockMvc mvc();


    default CategoryID givenACategory(final String aName, final String aDescription, final boolean isActive) throws Exception {
        final var aRequestBody = new CreateCategoryRequest(aName, aDescription, isActive);
        final var actualId = given("/categories", aRequestBody);
        return CategoryID.from(actualId);
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search
    ) throws Exception {
        return listCategories(page, perPage, search, "name", "asc");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage
    ) throws Exception {
        return listCategories(page, perPage, "", "name", "asc");
    }

    default ResultActions listCategories(
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        return list("/categories", page, perPage, search, sort, direction);
    }

    default CategoryResponse retrieveACategory(final Identifier anId) throws Exception {
        return retrieve("/categories/", anId, CategoryResponse.class);
    }

    default ResultActions deleteACategory(Identifier anId) throws Exception {
        return this.delete("/categories/", anId);
    }

    private <T> T retrieve(final String url, final Identifier anId, final Class<T> clazz) throws Exception {
        final var aRequest = get(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var json = mvc().perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return Json.readValue(json, clazz);
    }

    private ResultActions list(
            final String url,
            final int page,
            final int perPage,
            final String search,
            final String sort,
            final String direction
    ) throws Exception {
        final var aRequest = get(url)
                .param("page", String.valueOf(page))
                .param("perPage", String.valueOf(perPage))
                .param("search", search)
                .param("sort", sort)
                .param("dir", direction)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        return mvc().perform(aRequest);
    }

    default GenreID givenAGenre(final String aName, final boolean isActive, final List<CategoryID> categories) throws Exception {
        final var aRequestBody = new CreateGenreRequest(aName, mapTo(categories, CategoryID::getValue), isActive);
        final var actualId = given("/genres", aRequestBody);
        return GenreID.from(actualId);
    }

    default ResultActions updateACategory(Identifier anId, UpdateCategoryRequest aRequestBody) throws Exception {
        return update("/categories/", anId, aRequestBody);
    }

    private String given(final String url, final Object body) throws Exception {
        final var aRequest = post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(body));

        return Objects.requireNonNull(mvc().perform(aRequest)
                        .andExpect(status().isCreated())
                        .andReturn()
                        .getResponse()
                        .getHeader("Location"))
                .replace("%s/".formatted(url), "");
    }

    private <A, D> List<D> mapTo(final List<A> actual, final Function<A, D> mapper) {
        return actual.stream().map(mapper).toList();
    }

    private ResultActions delete(String url, Identifier anId) throws Exception {
        final var aRequest = MockMvcRequestBuilders.delete(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON);
        return mvc().perform(aRequest);
    }


    private ResultActions update(String url, Identifier anId, final Object aRequestBody) throws Exception {
        final var aRequest = put(url + anId.getValue())
                .contentType(MediaType.APPLICATION_JSON)
                .content(Json.writeValueAsString(aRequestBody));

        return mvc().perform(aRequest);
    }
}
