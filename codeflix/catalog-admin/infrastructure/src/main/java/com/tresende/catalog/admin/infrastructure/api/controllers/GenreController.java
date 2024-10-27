package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.application.genre.create.CreateGenreCommand;
import com.tresende.catalog.admin.application.genre.create.CreateGenreOutput;
import com.tresende.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.tresende.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.tresende.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.tresende.catalog.admin.application.genre.update.UpdateGenreCommand;
import com.tresende.catalog.admin.application.genre.update.UpdateGenreOutput;
import com.tresende.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.infrastructure.api.GenreAPI;
import com.tresende.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.tresende.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.tresende.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.tresende.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.tresende.catalog.admin.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;

    GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase, final DeleteGenreUseCase deleteGenreUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
    }

    @Override
    public ResponseEntity<CreateGenreOutput> createGenre(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(input.name(), input.active(), input.categories());
        final var output = createGenreUseCase.execute(aCommand);
        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> listGenres(final String search, final int page, final int perPage, final String sort, final String direction) {
        return null;
    }

    @Override
    public GenreResponse getById(final String id) {
        final var aGenre = getGenreByIdUseCase.execute(id);
        return GenreApiPresenter.present(aGenre);
    }

    @Override
    public ResponseEntity<UpdateGenreOutput> updateById(final String id, final UpdateGenreRequest input) {
        final var command = UpdateGenreCommand.with(id, input.name(), input.active(), input.categories());
        final var output = updateGenreUseCase.execute(command);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        deleteGenreUseCase.execute(id);
    }
}
