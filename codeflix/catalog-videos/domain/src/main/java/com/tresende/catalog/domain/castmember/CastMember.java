package com.tresende.catalog.domain.castmember;

import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.ValidationHandler;

import java.time.Instant;

public class CastMember {
    private final String id;
    private final String name;
    private final CastMemberType type;
    private final Instant createdAt;
    private final Instant updatedAt;

    private CastMember(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMember with(
            final String anId,
            final String aName,
            final CastMemberType aType,
            final Instant createdAt,
            final Instant updatedAt) {
        return new CastMember(anId, aName, aType, createdAt, updatedAt);
    }

    public static CastMember with(final CastMember aMember) {
        return with(aMember.id, aMember.name, aMember.type, aMember.createdAt, aMember.updatedAt);
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public CastMemberType type() {
        return type;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public CastMember validate(ValidationHandler aHandler) {
        if (id == null || id.isBlank()) {
            aHandler.append(new Error("'id' should not be empty"));
        }

        if (name == null || name.isBlank()) {
            aHandler.append(new Error("'name' should not be empty"));
        }

        if (type == null) {
            aHandler.append(new Error("'type' should not be null"));
        }
        return this;
    }
}
