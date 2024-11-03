package com.tresende.catalog.admin.application.castmember.create;

import com.tresende.catalog.admin.application.Fixture;
import com.tresende.catalog.admin.application.UseCaseTest;
import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

class CreateCastMemberUseCaseTest extends UseCaseTest {
    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCaseMember_shouldReturn() {
        //given
        final var expectedName = Fixture.name();
        final var expectedType = Fixture.CastMember.type();
        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);
        Mockito.when(castMemberGateway.create(any())).thenAnswer(returnsFirstArg());

        //when
        final var actualOutput = useCase.execute(aCommand);
        //then

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(castMemberGateway).create(argThat(aMember ->
                Objects.nonNull(aMember.getId())
                        && Objects.equals(expectedName, aMember.getName())
                        && Objects.equals(expectedType, aMember.getType())
                        && Objects.nonNull(aMember.getCreatedAt())
                        && Objects.nonNull(aMember.getUpdatedAt())
        ));
    }
}
