package com.tresende.catalog.application.castmember.delete;

import com.tresende.catalog.application.UseCaseTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenValidId_whenCallsDelete_shouldBeOk() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();
        final var expectedId = gabriel.id();

        doNothing().when(castMemberGateway)
                .deleteById(anyString());

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(castMemberGateway, times(1)).deleteById(expectedId);
    }

    @Test
    public void givenInvalidId_whenCallsDelete_shouldBeOk() {
        // given
        final String expectedId = null;

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId));

        // then
        verify(castMemberGateway, never()).deleteById(any());
    }
}
