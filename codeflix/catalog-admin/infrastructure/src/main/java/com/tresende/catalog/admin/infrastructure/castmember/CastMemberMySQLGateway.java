package com.tresende.catalog.admin.infrastructure.castmember;

import com.tresende.catalog.admin.domain.castmember.CastMember;
import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import com.tresende.catalog.admin.domain.castmember.CastMemberID;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;
import com.tresende.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository repository;

    public CastMemberMySQLGateway(final CastMemberRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public CastMember create(final CastMember aCastMember) {
        return null;
    }

    @Override
    public void deleteById(final CastMemberID anId) {

    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return Optional.empty();
    }

    @Override
    public CastMember update(final CastMember aCastMember) {
        return null;
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        return null;
    }
}
