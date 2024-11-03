package com.tresende.catalog.admin.application.castmember.create;

import com.tresende.catalog.admin.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
        String name,
        CastMemberType type
) {

    public static CreateCastMemberCommand with(String aName, CastMemberType aType) {
        return new CreateCastMemberCommand(aName, aType);
    }
}