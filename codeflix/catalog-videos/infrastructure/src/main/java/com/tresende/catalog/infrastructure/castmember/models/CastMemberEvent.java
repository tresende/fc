package com.tresende.catalog.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;

import static com.tresende.catalog.domain.utils.InstantUtils.fromTimestamp;

public record CastMemberEvent(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") Long createdAt,
        @JsonProperty("updated_at") Long updatedAt
) {
    public static CastMemberEvent from(final CastMember aMember) {
        return new CastMemberEvent(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toEpochMilli(),
                aMember.updatedAt().toEpochMilli()
        );
    }

    public CastMember toCastMember() {
        return CastMember.with(id, name, CastMemberType.of(type), fromTimestamp(createdAt), fromTimestamp(updatedAt));
    }
}
