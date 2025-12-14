package com.tresende.catalog.infrastructure.genre;

import com.tresende.catalog.domain.genre.Genre;
import com.tresende.catalog.domain.genre.GenreGateway;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.infrastructure.genre.persistence.GenreDocument;
import com.tresende.catalog.infrastructure.genre.persistence.GenreRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchOperations;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Objects;
import java.util.Optional;

@Component
public class GenreElasticsearchGateway implements GenreGateway {

    private static final String KEYWORD = ".keyword";
    private static final String NAME_PROP = "name";
    private final GenreRepository genreRepository;
    private final SearchOperations searchOperations;

    public GenreElasticsearchGateway(final GenreRepository genreRepository, final SearchOperations searchOperations) {
        this.genreRepository = Objects.requireNonNull(genreRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public Genre save(final Genre aGenre) {
        genreRepository.save(GenreDocument.from(aGenre));
        return aGenre;
    }

    @Override
    public void deleteById(final String anId) {
        if (genreRepository.existsById(anId)) {
            genreRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<Genre> findById(final String anId) {
        return genreRepository.findById(anId).map(GenreDocument::toGenre);
    }

    @Override
    public Pagination<Genre> findAll(final GenreSearchQuery aQuery) {

        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var pageRequest = PageRequest.of(currentPage, perPage, sort);

        final var query = StringUtils.isEmpty(terms) && CollectionUtils.isEmpty(aQuery.categories())
                ? Query.findAll().setPageable(pageRequest)
                : new CriteriaQuery(createCriteria(aQuery));

        final var res = searchOperations.search(query, GenreDocument.class);
        final var total = res.getTotalHits();
        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(GenreDocument::toGenre).toList();

        return new Pagination<>(
                aQuery.page(),
                aQuery.perPage(),
                total,
                categories
        );

    }

    private Criteria createCriteria(final GenreSearchQuery aQuery) {
        Criteria criteria = null;

        if (StringUtils.isNotEmpty(aQuery.terms())) {
            criteria = Criteria.where("name").contains(aQuery.terms());
        }
        if (!CollectionUtils.isEmpty(aQuery.categories())) {
            final var categoriesCriteria = Criteria.where("categories").in(aQuery.categories());
            criteria = criteria == null ? categoriesCriteria : criteria.and(categoriesCriteria);
        }

        return criteria;
    }


    private String buildSort(final String sort) {
        return switch (sort) {
            case NAME_PROP -> sort.concat(KEYWORD);
            default -> sort;
        };
    }
}
