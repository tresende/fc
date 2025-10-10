package com.tresende.catalog.infrastructure.castmember.models;


import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record GqlCastMemberInput(
        String id,
        String name,
        String type,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {
    public CastMember toCastMember() {
        final var type = CastMemberType.valueOf(type());
        return CastMember.with(id(), name(), type, createdAt(), updatedAt());
    }
}
