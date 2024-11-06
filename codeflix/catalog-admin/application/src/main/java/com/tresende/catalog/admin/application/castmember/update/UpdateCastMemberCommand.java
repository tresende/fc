package com.tresende.catalog.admin.application.castmember.update;

import com.tresende.catalog.admin.domain.castmember.CastMemberType;

public record UpdateCastMemberCommand(
        String id,
        String name,
        CastMemberType type
) {

    public static UpdateCastMemberCommand with(String anId, String aName, CastMemberType aType) {
        return new UpdateCastMemberCommand(anId, aName, aType);
    }
}