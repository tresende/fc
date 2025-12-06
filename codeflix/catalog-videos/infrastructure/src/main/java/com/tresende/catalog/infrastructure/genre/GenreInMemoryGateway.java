package com.tresende.catalog.infrastructure.genre;

import com.tresende.catalog.domain.genre.Genre;
import com.tresende.catalog.domain.genre.GenreGateway;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class GenreInMemoryGateway implements GenreGateway {

    private final Map<String, Genre> db;

    public GenreInMemoryGateway(final Map<String, Genre> db) {
        this.db = db;
    }

    @Override
    public Genre save(final Genre aGenre) {
        db.put(aGenre.id(), aGenre);
        return aGenre;
    }

    @Override
    public void deleteById(final String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<Genre> findById(final String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public Pagination<Genre> findAll(final GenreSearchQuery aQuery) {
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                db.size(),
                db.values().stream().toList()
        );
    }
}
