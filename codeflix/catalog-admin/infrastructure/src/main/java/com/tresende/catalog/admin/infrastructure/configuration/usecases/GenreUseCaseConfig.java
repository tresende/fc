package com.tresende.catalog.admin.infrastructure.configuration.usecases;

import com.tresende.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.tresende.catalog.admin.application.genre.create.DefaultCreateGenreUseCase;
import com.tresende.catalog.admin.application.genre.delete.DefaultDeleteGenreUseCase;
import com.tresende.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.tresende.catalog.admin.application.genre.retrieve.get.DefaultGetGenreByIdUseCase;
import com.tresende.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.tresende.catalog.admin.application.genre.retrieve.list.DefaultListGenreUseCase;
import com.tresende.catalog.admin.application.genre.retrieve.list.ListGenreUseCase;
import com.tresende.catalog.admin.application.genre.update.DefaultUpdateGenreUseCase;
import com.tresende.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(
            GenreGateway genreGateway,
            CategoryGateway categoryGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

    @Bean
    public ListGenreUseCase listGenreUseCase() {
        return new DefaultListGenreUseCase(genreGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }
}
