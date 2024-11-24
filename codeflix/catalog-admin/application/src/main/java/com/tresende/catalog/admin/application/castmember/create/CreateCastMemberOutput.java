package com.tresende.catalog.admin.application.castmember.create;

import com.tresende.catalog.admin.domain.castmember.CastMember;
import com.tresende.catalog.admin.domain.castmember.CastMemberID;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput from(final CastMemberID anId) {
        return new CreateCastMemberOutput(anId.getValue());
    }
    
    public static CreateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.getId());
    }
}