package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.category.CategorySearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CategoryInMemoryGateway implements CategoryGateway {

    private final ConcurrentHashMap<String, Category> db;

    public CategoryInMemoryGateway() {
        this.db = new ConcurrentHashMap<>();
    }

    @Override
    public Category save(final Category aCategory) {
        db.put(aCategory.id(), aCategory);
        return aCategory;
    }

    @Override
    public void deleteById(final String anId) {
        db.remove(anId);
    }

    @Override
    public Optional<Category> findById(final String anId) {
        return Optional.ofNullable(db.get(anId));
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var values = db.values();
        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                values.size(),
                new ArrayList<>(values)
        );
    }
}
