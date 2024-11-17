package com.tresende.catalog.admin.infrastructure.castmember;

import com.tresende.catalog.admin.domain.castmember.CastMember;
import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import com.tresende.catalog.admin.domain.castmember.CastMemberID;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;
import com.tresende.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.tresende.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import com.tresende.catalog.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return this.save(aCastMember);
    }

    @Override
    public void deleteById(final CastMemberID aMemberId) {
        final var anId = aMemberId.getValue();
        if (this.castMemberRepository.existsById(anId)) {
            this.castMemberRepository.deleteById(anId);
        }
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID aMemberId) {
        return castMemberRepository.findById(aMemberId.getValue()).map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return this.save(aCastMember);
    }

    @Override
    public Pagination<CastMember> findAll(SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );
        final var specifications = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);
        final var pageResult = castMemberRepository.findAll(Specification.where(specifications), page);
        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<CastMemberID> existsByIds(final Iterable<CastMemberID> castMemberIDS) {
        final var ids = StreamSupport.stream(castMemberIDS.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();
        return this.castMemberRepository.existsByIds(ids).stream()
                .map(CastMemberID::from)
                .toList();
    }

    private CastMember save(final CastMember aCastMember) {
        return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember)).toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
