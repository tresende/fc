package com.tresende.catalog.infrastructure.castmember.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberDTO(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("type") String type,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
    public CastMember toCastMember() {
        final var type = CastMemberType.valueOf(type());
        return CastMember.with(id(), name(), type, createdAt(), updatedAt());
    }
}
