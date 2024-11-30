package com.tresende.catalog.admin.infrastructure.castmember.model;

import com.tresende.catalog.admin.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name,
                                      CastMemberType type) {
}
