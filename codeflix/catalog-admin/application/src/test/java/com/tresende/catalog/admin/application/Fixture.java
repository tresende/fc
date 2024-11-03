package com.tresende.catalog.admin.application;

import com.github.javafaker.Faker;
import com.tresende.catalog.admin.domain.castmember.CastMemberType;

public final class Fixture {
    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static final class CastMember {
        public static CastMemberType type() {
            return FAKER.options().option(
                    CastMemberType.ACTOR, CastMemberType.DIRECTOR
            );
        }
    }
}
