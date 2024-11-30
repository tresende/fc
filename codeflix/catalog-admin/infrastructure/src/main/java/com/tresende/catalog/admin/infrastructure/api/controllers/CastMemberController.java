package com.tresende.catalog.admin.infrastructure.api.controllers;

import com.tresende.catalog.admin.application.castmember.create.CreateCastMemberCommand;
import com.tresende.catalog.admin.application.castmember.create.CreateCastMemberOutput;
import com.tresende.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.retreive.get.GetCastMemberByIdUseCase;
import com.tresende.catalog.admin.application.castmember.retreive.list.ListCastMembersUseCase;
import com.tresende.catalog.admin.application.castmember.update.UpdateCastMemberCommand;
import com.tresende.catalog.admin.application.castmember.update.UpdateCastMemberOutput;
import com.tresende.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.tresende.catalog.admin.domain.pagination.Pagination;
import com.tresende.catalog.admin.domain.pagination.SearchQuery;
import com.tresende.catalog.admin.infrastructure.api.CastMemberAPI;
import com.tresende.catalog.admin.infrastructure.castmember.model.CastMemberListResponse;
import com.tresende.catalog.admin.infrastructure.castmember.model.CastMemberResponse;
import com.tresende.catalog.admin.infrastructure.castmember.model.CreateCastMemberRequest;
import com.tresende.catalog.admin.infrastructure.castmember.model.UpdateCastMemberRequest;
import com.tresende.catalog.admin.infrastructure.castmember.presenters.CastMemberPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMembersUseCase listCastMembersUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
    }

    @Override
    public ResponseEntity<CreateCastMemberOutput> create(final CreateCastMemberRequest input) throws URISyntaxException {
        final var aCommand = CreateCastMemberCommand.with(input.name(), input.type());
        final var output = createCastMemberUseCase.execute(aCommand);
        return ResponseEntity.created(new URI("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(final String search, final int page, final int perPage, final String sort, final String direction) {
        final var aQuery = new SearchQuery(page, perPage, search, sort, direction);
        final var output = listCastMembersUseCase.execute(aQuery);
        return output.map(CastMemberPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<UpdateCastMemberOutput> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var input = UpdateCastMemberCommand.with(id, aBody.name(), aBody.type());
        final var output = updateCastMemberUseCase.execute(input);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        deleteCastMemberUseCase.execute(id);
    }
}
