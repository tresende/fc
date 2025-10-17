package com.tresende.catalog.infrastructure.castmember;

import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberDocument;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.apache.commons.lang3.StringUtils;
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
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    private static final String NAME_PROP = "name";
    private static final String KEYWORD = ".keyword";
    private final CastMemberRepository castMemberRepository;
    private final SearchOperations searchOperations;

    public CastMemberElasticsearchGateway(
            final CastMemberRepository castMemberRepository,
            final SearchOperations searchOperations
    ) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
        this.searchOperations = Objects.requireNonNull(searchOperations);
    }

    @Override
    public CastMember save(final CastMember aMember) {
        this.castMemberRepository.save(CastMemberDocument.from(aMember));
        return aMember;
    }

    @Override
    public void deleteById(final String anId) {
        final var exists = castMemberRepository.existsById(anId);
        if (exists) {
            castMemberRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<CastMember> findById(final String anId) {
        return castMemberRepository.findById(anId).map(CastMemberDocument::toCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(final CastMemberSearchQuery aQuery) {
        final var terms = aQuery.terms();
        final var currentPage = aQuery.page();
        final var perPage = aQuery.perPage();

        final var sort = Sort.by(Sort.Direction.fromString(aQuery.direction()), buildSort(aQuery.sort()));
        final var page = PageRequest.of(currentPage, perPage, sort);

        final var query = StringUtils.isNotEmpty(terms)
                ? new CriteriaQuery(where("name").contains(terms), page)
                : Query.findAll().setPageable(page);

        final var res = searchOperations.search(query, CastMemberDocument.class);
        final var total = res.getTotalHits();
        final var categories = res.stream()
                .map(SearchHit::getContent)
                .map(CastMemberDocument::toCastMember).toList();

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
