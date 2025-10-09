package com.tresende.catalog.application.castmember.delete;

import com.tresende.catalog.application.UnitUseCase;
import com.tresende.catalog.domain.castmember.CastMemberGateway;

import java.util.Objects;

public class DeleteCastMemberUseCase extends UnitUseCase<String> {

    private final CastMemberGateway castMemberGateway;

    public DeleteCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String anId) {
        if (anId == null) return;
        castMemberGateway.deleteById(anId);
    }
}
