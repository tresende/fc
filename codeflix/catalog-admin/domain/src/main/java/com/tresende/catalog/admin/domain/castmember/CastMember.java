package com.tresende.catalog.admin.domain.castmember;

import com.tresende.catalog.admin.domain.AggregateRoot;
import com.tresende.catalog.admin.domain.exceptions.NotificationException;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;
import com.tresende.catalog.admin.domain.validation.handler.Notification;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> implements Cloneable {

    private String name;
    private CastMemberType type;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreatedAt,
            final Instant anUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreatedAt;
        this.updatedAt = anUpdatedAt;
        this.deletedAt = aDeletedAt;
        selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final var anId = CastMemberID.unique();
        final var now = Instant.now();

        return new CastMember(anId, aName, aType, now, now, null);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        super.validate(handler);
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);

        if (notification.hasError()) {
            throw new NotificationException("", notification);
        }
    }

    public String getName() {
        return name;
    }

    public CastMemberType getType() {
        return type;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public CastMember clone() {
        try {
            return (CastMember) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
