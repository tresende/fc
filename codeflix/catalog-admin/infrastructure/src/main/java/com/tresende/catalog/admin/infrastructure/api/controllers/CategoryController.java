package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.application.category.create.CreateCategoryCommand;
import com.tresende.catalog.admin.application.category.create.CreateCategoryOutput;
import com.tresende.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.tresende.catalog.admin.domain.Pagination;
import com.tresende.catalog.admin.domain.validation.handler.Notification;
import com.tresende.catalog.admin.infrastructure.api.CategoryAPI;
import com.tresende.catalog.admin.infrastructure.category.models.CreateCategoryApiInput;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryApiInput input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : false
        );
//        final Function<Notification, ResponseEntity<?>> onError = notification ->
//                ResponseEntity.unprocessableEntity().body(notification);

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;
        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return createCategoryUseCase.execute(aCommand).fold(onError, onSuccess);
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
