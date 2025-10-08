package com.tresende.catalog.domain.castmember;

import com.tresende.catalog.domain.UnitTest;
import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.utils.IdUtils;
import com.tresende.catalog.domain.utils.InstantUtils;
import com.tresende.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateACastMember() {
        //given
        final var expectedID = IdUtils.uniqueId();
        final var expectedName = "Thiago";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        //when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        //then
        Assertions.assertNotNull(actualMember);
        Assertions.assertEquals(expectedID, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDates, actualMember.createdAt());
        Assertions.assertEquals(expectedDates, actualMember.updatedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithCastMember_thenInstantiateACastMember() {
        //given
        final var expectedID = IdUtils.uniqueId();
        final var expectedName = "Thiago";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();

        final var aMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);

        //when
        final var actualMember = CastMember.with(aMember);

        //then
        Assertions.assertEquals(aMember.id(), actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(expectedDates, actualMember.createdAt());
        Assertions.assertEquals(expectedDates, actualMember.updatedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenWithAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = IdUtils.uniqueId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        //when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenWithAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = IdUtils.uniqueId();
        final String expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        //when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenWithAndValidate_thenShouldReceiveError() {
        //given
        final String expectedID = null;
        final var expectedName = "Filmes";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        //when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenWithAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = " ";
        final var expectedName = "Filmes";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        //when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }


    @Test
    public void givenAnInvalidNullType_whenWithAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = IdUtils.uniqueId();
        final var expectedName = "Filmes";
        final CastMemberType expectedType = null;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        //when
        final var actualMember =
                CastMember.with(expectedID, expectedName, expectedType, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualMember.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}