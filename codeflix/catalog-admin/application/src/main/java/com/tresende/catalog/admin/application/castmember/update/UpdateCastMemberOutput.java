package com.tresende.catalog.admin.application.castmember.update;

import com.tresende.catalog.admin.domain.castmember.CastMember;
import com.tresende.catalog.admin.domain.castmember.CastMemberID;

public record UpdateCastMemberOutput(
        String id
) {
    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId());
    }

    public static UpdateCastMemberOutput from(final CastMemberID anId) {
        return new UpdateCastMemberOutput(anId.getValue());
    }
}