package com.tresende.catalog.admin.infrastructure.castmember.model;

import com.tresende.catalog.admin.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
        String name,
        CastMemberType type
) {
}
