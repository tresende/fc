package com.tresende.catalog.application.castmember.save;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import com.tresende.catalog.domain.exceptions.NotificationException;
import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.handler.Notification;

import java.util.Objects;

class SaveCastMemberUseCase extends UseCase<CastMember, CastMember> {

    private final CastMemberGateway castMemberGateway;

    SaveCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMember execute(final CastMember aMember) {
        if (aMember == null)
            throw NotificationException.with(new Error("'aCastMember' cannot be null"));

        final var notification = Notification.create();
        aMember.validate(notification);
        if (notification.hasError())
            throw NotificationException.with("Invalid cast membmer"notification.getErrors());

        return castMemberGateway.save(aMember);
    }
}
