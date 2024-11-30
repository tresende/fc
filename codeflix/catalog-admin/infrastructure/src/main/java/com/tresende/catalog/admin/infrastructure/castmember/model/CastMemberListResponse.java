package com.tresende.catalog.admin.infrastructure.castmember.model;

public record CastMemberListResponse(
        String id,
        String name,
        String type,
        String createdAt
) {
}
