package com.tresende.catalog.infrastructure.graphql;

import com.tresende.catalog.GraphQLControllerTest;
import com.tresende.catalog.application.castmember.list.ListCastMemberUseCase;
import com.tresende.catalog.application.castmember.list.ListCastMembersOutput;
import com.tresende.catalog.application.castmember.save.SaveCastMemberUseCase;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import com.tresende.catalog.domain.castmember.CastMemberType;
import com.tresende.catalog.domain.pagination.Pagination;
import com.tresende.catalog.domain.utils.IdUtils;
import com.tresende.catalog.domain.utils.InstantUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@GraphQLControllerTest(controllers = CastMemberGraphQLController.class)
class CastMemberGraphQLControllerTest {
    @MockitoBean
    private ListCastMemberUseCase listCastMemberUseCase;

    @MockitoBean
    private SaveCastMemberUseCase saveCastMemberUseCase;

    @Autowired
    private GraphQlTester graphql;

    @Test
    public void givenDefaultArgumentsWhenCallsListCastMembersShouldReturn() {
        //given
        final var expectedMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 2;

        when(listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedItemsCount, expectedMembers));

        final var query = """
                {
                    castMembers{
                        id
                        name
                        type
                        updatedAt
                        createdAt
                    }
                }
                """;

        final var res = graphql.document(query).execute();

        //when
        final var actualCastMembers = res.path("castMembers")
                .entityList(ListCastMembersOutput.class)
                .get();

        //then
        Assertions.assertTrue(
                actualCastMembers.size() == expectedMembers.size() &&
                        actualCastMembers.containsAll(expectedMembers)
        );

        final var capturer = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        verify(listCastMemberUseCase, times(1)).execute(capturer.capture());

        final var actualQuery = capturer.getValue();
        Assertions.assertEquals(expectedPage, actualQuery.page());
        Assertions.assertEquals(expectedPerPage, actualQuery.perPage());
        Assertions.assertEquals(expectedSort, actualQuery.sort());
        Assertions.assertEquals(expectedDirection, actualQuery.direction());
    }

    @Test
    public void givenCustomArgumentsWhenCallsListCastMembersShouldReturn() {
        //given
        final var expectedMembers = List.of(
                ListCastMembersOutput.from(Fixture.CastMembers.gabriel()),
                ListCastMembersOutput.from(Fixture.CastMembers.wesley())
        );

        final var expectedPage = 2;
        final var expectedPerPage = 15;
        final var expectedSort = "id";
        final var expectedDirection = "desc";
        final var expectedSearch = "asd";

        when(listCastMemberUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedMembers.size(), expectedMembers));

        final var query = """
                query allCastMembers($search: String, $page: Int, $perPage: Int, $sort: String, $direction: String) {
                    castMembers(search: $search, page: $page, perPage: $perPage, sort: $sort, direction: $direction){
                        id
                        name
                        type
                        createdAt
                        updatedAt
                    }
                }
                """;

        final var res = graphql.document(query)
                .variable("search", expectedSearch)
                .variable("page", expectedPage)
                .variable("perPage", expectedPerPage)
                .variable("sort", expectedSort)
                .variable("direction", expectedDirection)
                .execute();

        //when
        final var actualCastMembers = res.path("castMembers")
                .entityList(ListCastMembersOutput.class)
                .get();

        //then
        Assertions.assertTrue(
                actualCastMembers.size() == expectedMembers.size() &&
                        actualCastMembers.containsAll(expectedMembers)
        );

        final var capturer = ArgumentCaptor.forClass(CastMemberSearchQuery.class);
        verify(listCastMemberUseCase, times(1)).execute(capturer.capture());
    }

    @Test
    public void givenCastMemberInputWhenCallsSaveCastMemberMutationShouldPersistAndReturn() {
        //given
        final var expectedId = IdUtils.uniqueId();
        final var expectedName = "thiago";
        final var expectedType = CastMemberType.ACTOR;
        final var expectedCreatedAt = InstantUtils.now();
        final var expectedUpdatedAt = InstantUtils.now();

        //when
        final var input = Map.of(
                "id", expectedId,
                "name", expectedName,
                "type", expectedType.name(),
                "createdAt", expectedCreatedAt.toString(),
                "updatedAt", expectedUpdatedAt.toString()
        );

        final var query = """
                mutation SaveCastMember($input: CastMemberInput!) {
                    castMember: saveCastMember(input: $input) {
                        id
                        name
                        type
                        createdAt
                        updatedAt
                    }
                }
                """;


        when(saveCastMemberUseCase.execute(any()))
                .thenAnswer(returnsFirstArg());

        graphql.document(query)
                .variable("input", input)
                .execute()
                .path("castMember.id").entity(String.class).isEqualTo(expectedId)
                .path("castMember.name").entity(String.class).isEqualTo(expectedName)
                .path("castMember.type").entity(CastMemberType.class).isEqualTo(expectedType)
                .path("castMember.createdAt").entity(Instant.class).isEqualTo(expectedCreatedAt)
                .path("castMember.updatedAt").entity(Instant.class).isEqualTo(expectedUpdatedAt)
        ;

        //then

        final var capturer = ArgumentCaptor.forClass(CastMember.class);
        verify(saveCastMemberUseCase, times(1)).execute(capturer.capture());

        final var actualCastMember = capturer.getValue();
        Assertions.assertEquals(expectedId, actualCastMember.id());
        Assertions.assertEquals(expectedName, actualCastMember.name());
        Assertions.assertEquals(expectedType, actualCastMember.type());
        Assertions.assertEquals(expectedCreatedAt, actualCastMember.createdAt());
        Assertions.assertEquals(expectedUpdatedAt, actualCastMember.updatedAt());
    }
}
