package com.tresende.catalog.admin.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tresende.catalog.admin.ControllerTest;
import com.tresende.catalog.admin.application.category.create.CreateCategoryOutput;
import com.tresende.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.infrastructure.category.models.CreateCategoryApiInput;
import io.vavr.API;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Objects;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ControllerTest(controllers = CategoryAPI.class)
class CategoryAPITest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    private MockMvc mvc;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() throws Exception {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aInput = new CreateCategoryApiInput(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any())).thenReturn(API.Right(CreateCategoryOutput.from(CategoryID.from("123"))));

        final var request = MockMvcRequestBuilders
                .post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(aInput));

        mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/categories/123"),
                        jsonPath("$.id", equalTo("123"))
                );

        verify(createCategoryUseCase, times(1)).execute(argThat(params ->
                Objects.equals(expectedName, params.name())
                        && Objects.equals(expectedDescription, params.description())
                        && Objects.equals(expectedIsActive, params.isActive())
        ));
    }
}
