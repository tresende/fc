package com.tresende.catalog.admin.infrastructure.category;

import com.tresende.catalog.admin.domain.Pagination;
import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.category.CategorySearchQuery;
import com.tresende.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.tresende.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryMySqlGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySqlGateway(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category create(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Category update(Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(CategoryID anId) {

    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        return Optional.empty();
    }

    @Override
    public Pagination<Category> findAll(CategorySearchQuery aQuery) {
        return null;
    }

    private Category save(Category aCategory) {
        final var categoryJpaEntity = CategoryJpaEntity.from(aCategory);
        return repository.save(categoryJpaEntity).toAggregate();
    }
}
