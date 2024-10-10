package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.application.category.create.CreateCategoryCommand;
import com.tresende.catalog.admin.application.category.create.CreateCategoryOutput;
import com.tresende.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.tresende.catalog.admin.application.category.delete.DeleteCategoryCommand;
import com.tresende.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.tresende.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.tresende.catalog.admin.application.category.retrieve.list.ListCategoryUseCase;
import com.tresende.catalog.admin.application.category.update.UpdateCategoryCommand;
import com.tresende.catalog.admin.application.category.update.UpdateCategoryOutput;
import com.tresende.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.tresende.catalog.admin.domain.Pagination;
import com.tresende.catalog.admin.domain.category.CategorySearchQuery;
import com.tresende.catalog.admin.domain.validation.handler.Notification;
import com.tresende.catalog.admin.infrastructure.api.CategoryAPI;
import com.tresende.catalog.admin.infrastructure.category.models.CategoryListResponse;
import com.tresende.catalog.admin.infrastructure.category.models.CategoryResponse;
import com.tresende.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import com.tresende.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.tresende.catalog.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoryUseCase listCategoryUseCase;

    public CategoryController(
            CreateCategoryUseCase createCategoryUseCase,
            GetCategoryByIdUseCase getCategoryByIdUseCase,
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase,
            ListCategoryUseCase listCategoryUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(CreateCategoryRequest input) {
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
    public Pagination<CategoryListResponse> listCategories(String search, int page, int perPage, String sort, String direction) {
        final var aQuery = new CategorySearchQuery(page, perPage, search, sort, direction);
        return this.listCategoryUseCase.execute(aQuery).map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(String id) {
        return CategoryApiPresenter.present(getCategoryByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(String id, UpdateCategoryRequest input) {
        final var aCommand = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : false
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;
        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return updateCategoryUseCase.execute(aCommand).fold(onError, onSuccess);

    }

    @Override
    public void deleteById(String anId) {
        final var aCommand = DeleteCategoryCommand.with(anId);
        deleteCategoryUseCase.execute(aCommand);
    }
}
