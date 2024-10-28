package com.tresende.catalog.admin.e2e.genre;

import com.tresende.catalog.admin.E2ETest;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.e2e.MockDsl;
import com.tresende.catalog.admin.infrastructure.configuration.json.Json;
import com.tresende.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.tresende.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
class GenreE2ETest implements MockDsl {

    @Container
    private static final MySQLContainer MYSQL_CONTAINER =
            new MySQLContainer("mysql:latest")
                    .withUsername("root")
                    .withDatabaseName("adm_videos")
                    .withPassword("123456");

    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return mvc;
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, genreRepository.count());

        final var filmes = givenACategory("Filmes", null, true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    //
//    @Test
//    public void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
//
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        givenACategory("Filmes", null, true);
//        givenACategory("Documentários", null, true);
//        givenACategory("Series", null, true);
//
//        listCategories(0, 1)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.current_page", equalTo(0)))
//                .andExpect(jsonPath("$.per_page", equalTo(1)))
//                .andExpect(jsonPath("$.items", hasSize(1)))
//                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")));
//
//        listCategories(1, 1)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.current_page", equalTo(1)))
//                .andExpect(jsonPath("$.per_page", equalTo(1)))
//                .andExpect(jsonPath("$.items", hasSize(1)))
//                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));
//
//        listCategories(2, 1)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.current_page", equalTo(2)))
//                .andExpect(jsonPath("$.per_page", equalTo(1)))
//                .andExpect(jsonPath("$.items", hasSize(1)))
//                .andExpect(jsonPath("$.items[0].name", equalTo("Series")));
//
//        listCategories(3, 1)
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.current_page", equalTo(3)))
//                .andExpect(jsonPath("$.per_page", equalTo(1)))
//                .andExpect(jsonPath("$.items", hasSize(0)));
//    }
//
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        givenACategory("Filmes", null, true);
//        givenACategory("Documentários", null, true);
//        givenACategory("Series", null, true);
//
//        listCategories(0, 1, "Fil")
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.current_page", equalTo(0)))
//                .andExpect(jsonPath("$.per_page", equalTo(1)))
//                .andExpect(jsonPath("$.items", hasSize(1)))
//                .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        givenACategory("Filmes", "C", true);
//        givenACategory("Documentários", "Z", true);
//        givenACategory("Séries", "A", true);
//
//        listCategories(0, 3, "", "description", "desc")
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.current_page", equalTo(0)))
//                .andExpect(jsonPath("$.per_page", equalTo(3)))
//                .andExpect(jsonPath("$.total", equalTo(3)))
//                .andExpect(jsonPath("$.items", hasSize(3)))
//                .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")))
//                .andExpect(jsonPath("$.items[1].name", equalTo("Filmes")))
//                .andExpect(jsonPath("$.items[2].name", equalTo("Séries")));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToGetACategoryByIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        final var expectedName = "Filmes";
//        final var expectedDescription = "A categoria mais assistida";
//        final var expectedIsActive = true;
//
//        final var categoryId = givenACategory(expectedName, expectedDescription, expectedIsActive);
//        final var actualCategory = retrieveACategory(categoryId);
//
//        Assertions.assertEquals(expectedName, actualCategory.name());
//        Assertions.assertEquals(expectedDescription, actualCategory.description());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.createdAt());
//        Assertions.assertNotNull(actualCategory.updatedAt());
//        Assertions.assertNull(actualCategory.deletedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//
//        final var aRequest = get("/categories/123")
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        mvc.perform(aRequest)
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        final var actualId = givenACategory("Movies", null, true);
//
//        final var expectedName = "Filmes";
//        final var expectedDescription = "A categoria mais assistida";
//        final var expectedIsActive = true;
//
//        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
//
//        updateACategory(actualId, aRequestBody)
//                .andExpect(status().isCreated());
//
//        final var actualCategory = categoryRepository.findById(actualId).get();
//
//        Assertions.assertEquals(expectedName, actualCategory.getName());
//        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertNotNull(actualCategory.getUpdatedAt());
//        Assertions.assertNull(actualCategory.getDeletedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        final var actualId = givenACategory("Movies", null, true);
//
//        final var expectedName = "Filmes";
//        final var expectedDescription = "A categoria mais assistida";
//        final var expectedIsActive = false;
//
//        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
//
//        updateACategory(actualId, aRequestBody)
//                .andExpect(status().isCreated());
//
//        final var actualCategory = categoryRepository.findById(actualId).get();
//
//        Assertions.assertEquals(expectedName, actualCategory.getName());
//        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertNotNull(actualCategory.getUpdatedAt());
//        Assertions.assertNotNull(actualCategory.getDeletedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        final var actualId = givenACategory("Movies", null, false);
//
//        final var expectedName = "Filmes";
//        final var expectedDescription = "A categoria mais assistida";
//        final var expectedIsActive = true;
//
//        final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);
//
//        updateACategory(actualId, aRequestBody)
//                .andExpect(status().isCreated());
//
//        final var actualCategory = categoryRepository.findById(actualId).get();
//
//        Assertions.assertEquals(expectedName, actualCategory.getName());
//        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
//        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
//        Assertions.assertNotNull(actualCategory.getCreatedAt());
//        Assertions.assertNotNull(actualCategory.getUpdatedAt());
//        Assertions.assertNull(actualCategory.getDeletedAt());
//    }
//
//    @Test
//    public void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        final var actualId = givenACategory("Filmes", null, true);
//
//        deleteACategory(actualId)
//                .andExpect(status().isNoContent());
//
//        Assertions.assertFalse(this.categoryRepository.existsById(actualId));
//    }
//
//    @Test
//    public void asACatalogAdminIShouldNotSeeAnErrorByDeletingANotExistentCategory() throws Exception {
//        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
//        Assertions.assertEquals(0, categoryRepository.count());
//
//        deleteACategory("123")
//                .andExpect(status().isNoContent());
//
//        Assertions.assertEquals(0, this.categoryRepository.count());
//    }
//
//
//    private ResultActions listCategories(
//            final int page,
//            final int perPage,
//            final String search
//    ) throws Exception {
//        return listCategories(page, perPage, search, "name", "asc");
//    }
//
//    private ResultActions listCategories(
//            final int page,
//            final int perPage
//    ) throws Exception {
//        return listCategories(page, perPage, "", "name", "asc");
//    }
//
//    private ResultActions listCategories(
//            final int page,
//            final int perPage,
//            final String search,
//            final String sort,
//            final String direction
//    ) throws Exception {
//        final var aRequest = get("/categories")
//                .param("page", String.valueOf(page))
//                .param("perPage", String.valueOf(perPage))
//                .param("search", search)
//                .param("sort", sort)
//                .param("dir", direction)
//                .contentType(MediaType.APPLICATION_JSON)
//                .accept(MediaType.APPLICATION_JSON);
//
//        return mvc.perform(aRequest);
//    }
//
    private GenreResponse retrieveAGenre(final String anId) throws Exception {
        final var aRequest = get("/genres/" + anId)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        final var json = mvc.perform(aRequest)
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return Json.readValue(json, GenreResponse.class);
    }

//
//    private ResultActions updateACategory(String anId, UpdateCategoryRequest aRequestBody) throws Exception {
//        final var aRequest = put("/categories/" + anId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(Json.writeValueAsString(aRequestBody));
//
//        return mvc.perform(aRequest);
//    }
//
//    private ResultActions deleteACategory(String anId) throws Exception {
//        final var aRequest = delete("/categories/" + anId)
//                .contentType(MediaType.APPLICATION_JSON);
//
//        return mvc.perform(aRequest);
//    }
}
