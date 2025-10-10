package com.tresende.catalog.application.castmember.list;


import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record ListCastMembersOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
    public static ListCastMembersOutput from(final CastMember castMember) {
        return new ListCastMembersOutput(
                castMember.id(),
                castMember.name(),
                castMember.type(),
                castMember.createdAt(),
                castMember.updatedAt()
        );
    }
}
