package com.tresende.catalog.application.castmember.save;

import com.tresende.catalog.application.UseCaseTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import com.tresende.catalog.domain.castmember.CastMemberType;
import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


public class SaveCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private SaveCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt() {
        // given
        final var aCastMember = Fixture.CastMembers.gabriel();

        when(castMemberGateway.save(any()))
                .thenAnswer(returnsFirstArg());

        // when
        this.useCase.execute(aCastMember);

        // then
        verify(castMemberGateway, times(1)).save(eq(aCastMember));
    }

    @Test
    public void givenNullCastMember_whenCallsSave_shouldReturnError() {
        // given
        final CastMember aCastMember = null;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'aCastMember' cannot be null";

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).save(eq(aCastMember));
    }

    @Test
    public void givenInvalidId_whenCallsSave_shouldReturnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        final var aCastMember = CastMember.with(
                "",
                "thiago",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).save(eq(aCastMember));
    }

    @Test
    public void givenInvalidName_whenCallsSave_shouldReturnError() {
        // given
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var aCastMember = CastMember.with(
                UUID.randomUUID().toString().replace("-", ""),
                "",
                CastMemberType.ACTOR,
                InstantUtils.now(),
                InstantUtils.now()
        );

        // when
        final var actualError = assertThrows(DomainException.class, () -> this.useCase.execute(aCastMember));

        // then
        assertEquals(expectedErrorCount, actualError.getErrors().size());
        assertEquals(expectedErrorMessage, actualError.getErrors().get(0).message());

        verify(castMemberGateway, times(0)).save(eq(aCastMember));
    }
}
