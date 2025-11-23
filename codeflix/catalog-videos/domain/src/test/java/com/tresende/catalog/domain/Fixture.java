package com.tresende.catalog.domain;

import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;
import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.genre.Genre;
import com.tresende.catalog.domain.utils.IdUtils;
import net.datafaker.Faker;

import java.util.Collections;
import java.util.UUID;

import static com.tresende.catalog.domain.utils.InstantUtils.now;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    public static String name() {
        return FAKER.name().fullName();
    }

    public static Integer year() {
        return FAKER.random().nextInt(2020, 2030);
    }

    public static Double duration() {
        return FAKER.options().option(120.0, 15.5, 35.5, 10.0, 2.0);
    }

    public static boolean bool() {
        return FAKER.bool().bool();
    }

    public static String title() {
        return FAKER.options().option(
                "System Design no Mercado Livre na prática",
                "Não cometa esses erros ao trabalhar com Microsserviços",
                "Testes de Mutação. Você não testa seu software corretamente"
        );
    }

    public static String checksum() {
        return "03fe62de";
    }

    public static final class Categories {

        public static Category aulas() {
            return Category.with(
                    IdUtils.uniqueId(),
                    "Aulas",
                    "Conteúdo Gravado",
                    true,
                    now(),
                    now(),
                    null
            );
        }

        public static Category talks() {
            return Category.with(
                    IdUtils.uniqueId(),
                    "Talks",
                    "Conteúdo Gravado",
                    false,
                    now(),
                    now(),
                    now()
            );
        }

        public static Category lives() {
            return Category.with(
                    IdUtils.uniqueId(),
                    "Lives",
                    "Conteúdo ao vivo",
                    true,
                    now(),
                    now(),
                    null
            );
        }
    }

    public static final class CastMembers {

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }

        public static CastMember wesley() {
            return CastMember.with(UUID.randomUUID().toString(), "Wesley FullCycle", CastMemberType.ACTOR, now(), now());
        }

        public static CastMember leonan() {
            return CastMember.with(UUID.randomUUID().toString(), "Leonan FullCycle", CastMemberType.DIRECTOR, now(), now());
        }


        public static CastMember gabriel() {
            return CastMember.with(UUID.randomUUID().toString(), "Gabriel FullCycle", CastMemberType.ACTOR, now(), now());
        }
    }

    public static final class Genres {
        public static Genre tech() {
            return Genre.with(IdUtils.uniqueId(), "Technology", true, Collections.emptySet(), now(), now(), now());
        }

        public static Genre business() {
            return Genre.with(IdUtils.uniqueId(), "Business", true, Collections.emptySet(), now(), now(), now());
        }
    }
}
