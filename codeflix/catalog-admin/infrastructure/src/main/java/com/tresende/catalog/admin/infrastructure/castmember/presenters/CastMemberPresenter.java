package com.tresende.catalog.admin.infrastructure.castmember.presenters;

import com.tresende.catalog.admin.application.castmember.retreive.get.CastMemberOutput;
import com.tresende.catalog.admin.infrastructure.castmember.model.CastMemberResponse;

public interface CastMemberPresenter {
    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }
}