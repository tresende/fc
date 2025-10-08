package com.tresende.catalog.domain.castmember;

import com.tresende.catalog.domain.pagination.Pagination;

import java.util.Optional;

public interface CastMemberGateway {

    CastMember save(CastMember aMember);

    void deleteBy(String anId);

    Optional<CastMember> findById(String anId);

    Pagination<CastMember> findAll(CastMemberSearchQuery aQuery);
}
