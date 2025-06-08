package com.tresende.catalog.infrastructure.category;

import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.category.CategorySearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.infrastructure.category.persistence.CategoryDocument;
import com.tresende.catalog.infrastructure.category.persistence.CategoryRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.elasticsearch.core.query.Criteria.where;

@Component
@Profile("!development")
public class CategoryElasticsearchGateway implements CategoryGateway {

    private static final String KEYWORD = ".keyword";
    private static final String NAME_PROP = "name";
    private static final String DESCRIPTION_PROP = "description";
    private final CategoryRepository categoryRepository;
    private final SearchOperations searchOperations;

    public CategoryElasticsearchGateway(final CategoryRepository categoryRepository, final SearchOperations searchOperations) {
        this.categoryRepository = Objects.requireNonNull(categoryRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Category save(final Category aCategory) {
        categoryRepository.save(CategoryDocument.from(aCategory));
        return aCategory;
    }

    @Override
    public void deleteById(final String anId) {
        categoryRepository.deleteById(anId);
    }

    @Override
    public Optional<Category> findById(final String anId) {
        return categoryRepository.findById(anId)
                .map(CategoryDocument::toCategory);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, perPage, sort);

        Query query = null;
        if (StringUtils.isNotEmpty(terms)) {
            final var criteria = where("name").contains(terms)
                    .or(where("description").contains(terms));
            query = new CriteriaQuery(criteria, page);
        } else {
            query = Query.findAll().setPageable(page);
        }

        final var res = searchOperations.search(query, CategoryDocument.class);
        final var total = res.getTotalHits();
        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(CategoryDocument::toCategory).toList();

        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                total,
                categories
        );
    }

    private String buildSort(final String sort) {
        return switch (sort) {
            case NAME_PROP -> sort.concat(KEYWORD);
            default -> sort;
        };
    }
}
