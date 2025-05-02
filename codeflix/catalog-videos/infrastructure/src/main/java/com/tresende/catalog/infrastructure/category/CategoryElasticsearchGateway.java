package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.category.CategorySearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.infrastructure.category.persistence.CategoryDocument;
import com.tresende.catalog.infrastructure.category.persistence.CategoryRepository;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CategoryElasticsearchGateway implements CategoryGateway {

    private final CategoryRepository categoryRepository;
    private final SearchOperations searchOperations;

    public CategoryElasticsearchGateway(final CategoryRepository categoryRepository, final SearchOperations searchOperations) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Category save(final Category aCategory) {
        this.categoryRepository.save(CategoryDocument.from(aCategory));
        return aCategory;
    }

    @Override
    public void deleteById(final String anId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<Category> findById(final String anId) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
//        final var values = db.values();
//        return new Pagination<>(
//                aQuery.page(),
//                aQuery.perPage(),
//                values.size(),
//                new ArrayList<>(values)
//        );
        throw new UnsupportedOperationException();
    }
}
