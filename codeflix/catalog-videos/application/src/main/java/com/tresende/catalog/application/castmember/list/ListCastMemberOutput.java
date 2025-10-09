package com.tresende.catalog.application.castmember.list;


import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record ListCastMemberOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
    public static ListCastMemberOutput from(final CastMember aCastMember) {
        return new ListCastMemberOutput(
                aCastMember.id(),
                aCastMember.name(),
                aCastMember.type(),
                aCastMember.createdAt(),
                aCastMember.updatedAt()
        );
    }
}
