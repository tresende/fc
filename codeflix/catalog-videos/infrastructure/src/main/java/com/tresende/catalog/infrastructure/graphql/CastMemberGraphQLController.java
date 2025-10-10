package com.tresende.catalog.infrastructure.graphql;

import com.tresende.catalog.application.castmember.list.ListCastMemberUseCase;
import com.tresende.catalog.application.castmember.list.ListCastMembersOutput;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CastMemberGraphQLController {

    private final ListCastMemberUseCase listCastMemberUseCase;

    public CastMemberGraphQLController(final ListCastMemberUseCase listCastMemberUseCase) {
        this.listCastMemberUseCase = Objects.requireNonNull(listCastMemberUseCase);
    }

    @QueryMapping
    public List<ListCastMembersOutput> castMembers(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ) {
        final var aQuery = new CastMemberSearchQuery(page, perPage, search, sort, direction);
        return listCastMemberUseCase.execute(aQuery).data();
    }
}
