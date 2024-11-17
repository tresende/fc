package com.tresende.catalog.admin.application.castmember.retreive.list;

import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public non-sealed class DefaultListCastMembersUseCase extends ListCastMembersUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultListCastMembersUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(final SearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery)
                .map(CastMemberListOutput::from);
    }
}
