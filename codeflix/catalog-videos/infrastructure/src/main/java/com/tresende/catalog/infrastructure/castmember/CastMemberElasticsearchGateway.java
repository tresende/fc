package com.tresende.catalog.infrastructure.castmember;

import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberDocument;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class CastMemberElasticsearchGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberElasticsearchGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
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
        throw new Error();
    }
}
