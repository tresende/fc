package com.tresende.catalog.admin.application.castmember.create;

import com.tresende.catalog.admin.domain.castmember.CastMember;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput with(final CastMember aMember) {
        return new CreateCastMemberOutput(aMember.getId().getValue());
    }
}