package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.infrastructure.api.GenreAPI;
import com.tresende.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.tresende.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.tresende.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.tresende.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
class GenreController implements GenreAPI {
    @Override
    public ResponseEntity<?> createGenre(final CreateGenreRequest input) {
        return null;
    }

    @Override
    public Pagination<GenreListResponse> listGenres(final String search, final int page, final int perPage, final String sort, final String direction) {
        return null;
    }

    @Override
    public GenreResponse getById(final String id) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        return null;
    }

    @Override
    public void deleteById(final String id) {

    }
}
