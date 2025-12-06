package com.tresende.catalog.infrastructure.configuration.usecases;

import com.tresende.catalog.application.genre.delete.DeleteGenreUseCase;
import com.tresende.catalog.application.genre.list.ListGenreUseCase;
import com.tresende.catalog.application.genre.save.SaveGenreUseCase;
import com.tresende.catalog.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration(enforceUniqueMethods = false)
public class GenreUseCasesConfig {

    private final GenreGateway genreGateway;

    public GenreUseCasesConfig(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Bean
    DeleteGenreUseCase deleteGenreUseCase() {
        return new DeleteGenreUseCase(genreGateway);
    }

    @Bean
    SaveGenreUseCase saveGenreUseCase() {
        return new SaveGenreUseCase(genreGateway);
    }

    @Bean
    ListGenreUseCase listGenreUseCaseTest() {
        return new ListGenreUseCase(genreGateway);
    }
}
