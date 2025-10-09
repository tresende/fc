package com.tresende.catalog.infrastructure.configuration.usecases;

import com.tresende.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.tresende.catalog.application.castmember.list.ListCastMemberUseCase;
import com.tresende.catalog.application.castmember.save.SaveCastMemberUseCase;
import com.tresende.catalog.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(enforceUniqueMethods = false, proxyBeanMethods = false)
public class CastMemberUseCasesConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCasesConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    SaveCastMemberUseCase saveCastMemberUseCase() {
        return new SaveCastMemberUseCase(castMemberGateway);
    }

    @Bean
    ListCastMemberUseCase listCastMemberUseCaseTest() {
        return new ListCastMemberUseCase(castMemberGateway);
    }
}
