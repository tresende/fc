package com.tresende.catalog.admin.infrastructure.configuration.usecases;


import com.tresende.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.retreive.get.DefaultGetCastMemberByIdUseCase;
import com.tresende.catalog.admin.application.castmember.retreive.get.GetCastMemberByIdUseCase;
import com.tresende.catalog.admin.application.castmember.retreive.list.DefaultListCastMembersUseCase;
import com.tresende.catalog.admin.application.castmember.retreive.list.ListCastMembersUseCase;
import com.tresende.catalog.admin.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.tresende.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}
