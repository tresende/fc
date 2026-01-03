package com.tresende.catalog;

import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.tresende.catalog.infrastructure.category.persistence.CategoryRepository;
import com.tresende.catalog.infrastructure.genre.persistence.GenreRepository;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;

public class IntegrationTestConfiguration {

    @Bean
    public CategoryRepository categoryRepository() {
        return Mockito.mock(CategoryRepository.class);
    }

    @Bean
    public CastMemberRepository castMemberRepository() {
        return Mockito.mock(CastMemberRepository.class);
    }

    @Bean
    public GenreRepository genreRepository() {
        return Mockito.mock(GenreRepository.class);
    }
}
