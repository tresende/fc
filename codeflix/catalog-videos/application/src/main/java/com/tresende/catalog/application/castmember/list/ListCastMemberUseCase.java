package com.tresende.catalog.application.castmember.list;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Objects;

public class ListCastMemberUseCase extends UseCase<CastMemberSearchQuery, Pagination<CastMember>> {

    private final CastMemberGateway castMemberGateway;

    public ListCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMember> execute(final CastMemberSearchQuery aQuery) {
        return castMemberGateway.findAll(aQuery);
    }
}
