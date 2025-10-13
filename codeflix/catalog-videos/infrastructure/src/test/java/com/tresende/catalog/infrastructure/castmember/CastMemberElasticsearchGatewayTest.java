package com.tresende.catalog.infrastructure.castmember;

import com.tresende.catalog.AbstractElasticsearchTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberDocument;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CastMemberElasticsearchGatewayTest extends AbstractElasticsearchTest {
    @Autowired
    private CastMemberElasticsearchGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        // when
        final var actualOutput = this.castMemberGateway.save(gabriel);

        // then
        Assertions.assertEquals(gabriel, actualOutput);

        final var actualCastMember = this.castMemberRepository.findById(gabriel.id()).get();
        Assertions.assertEquals(gabriel.id(), actualCastMember.id());
        Assertions.assertEquals(gabriel.name(), actualCastMember.name());
        Assertions.assertEquals(gabriel.type(), actualCastMember.type());
        Assertions.assertEquals(gabriel.createdAt(), actualCastMember.createdAt());
        Assertions.assertEquals(gabriel.updatedAt(), actualCastMember.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        this.castMemberRepository.save(CastMemberDocument.from(gabriel));

        final var expectedId = gabriel.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        // when
        this.castMemberGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "any";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.castMemberGateway.deleteById(expectedId));
    }


    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        this.castMemberRepository.save(CastMemberDocument.from(gabriel));

        final var expectedId = gabriel.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        // when
        final var actualOutput = this.castMemberGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(gabriel.id(), actualOutput.id());
        Assertions.assertEquals(gabriel.name(), actualOutput.name());
        Assertions.assertEquals(gabriel.type(), actualOutput.type());
        Assertions.assertEquals(gabriel.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(gabriel.updatedAt(), actualOutput.updatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = "any";

        // when
        final var actualOutput = this.castMemberGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }
}