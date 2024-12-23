package com.tresende.catalog.admin.infrastructure.category;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;
import com.tresende.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.tresende.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import com.tresende.catalog.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository repository) {
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
        final var existsById = repository.existsById(anId.getValue());
        if (existsById) {
            repository.deleteById(anId.getValue());
        }
    }

    @Override
    public Optional<Category> findById(CategoryID anId) {
        final var stringId = anId.getValue();
        return repository.findById(stringId).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Category> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);
        final var pageResult = repository.findAll(Specification.where(specifications), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> categoryIds) {
        final var ids = StreamSupport.stream(categoryIds.spliterator(), false)
                .map(CategoryID::getValue)
                .toList();

        return repository.existsByIds(ids)
                .stream()
                .map(CategoryID::from)
                .toList();
    }

    private Category save(Category aCategory) {
        final var categoryJpaEntity = CategoryJpaEntity.from(aCategory);
        return repository.save(categoryJpaEntity).toAggregate();
    }

    private Specification<CategoryJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.<CategoryJpaEntity>like("name", terms)
                .or(SpecificationUtils.like("description", terms));

    }
}
