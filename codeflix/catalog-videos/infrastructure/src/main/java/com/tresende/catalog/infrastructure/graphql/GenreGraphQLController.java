package com.tresende.catalog.infrastructure.graphql;

import com.tresende.catalog.application.genre.list.ListGenreUseCase;
import com.tresende.catalog.application.genre.save.SaveGenreUseCase;
import com.tresende.catalog.domain.genre.GenreSearchQuery;
import com.tresende.catalog.infrastructure.genre.models.GenreDTO;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;
import java.util.Set;

@Controller
public class GenreGraphQLController {

    private final ListGenreUseCase listGenreUseCase;
    private final SaveGenreUseCase saveGenreUseCase;

    public GenreGraphQLController(final ListGenreUseCase listGenreUseCase, final SaveGenreUseCase saveGenreUseCase) {
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.saveGenreUseCase = Objects.requireNonNull(saveGenreUseCase);
    }

    @QueryMapping
    public List<ListGenreUseCase.Output> genres(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction,
            @Argument final Set<String> categories
    ) {
        final var aQuery = new GenreSearchQuery(page, perPage, search, sort, direction, categories);
        return listGenreUseCase.execute(aQuery).data();
    }

    @MutationMapping
    public SaveGenreUseCase.Output saveGenre(@Argument final GenreDTO input) {
        return this.saveGenreUseCase.execute(new SaveGenreUseCase.Input(
                input.id(),
                input.name(),
                input.active() != null ? input.active() : true,
                input.categories() != null ? input.categories() : Set.of(),
                input.createdAt(),
                input.updatedAt(),
                input.deletedAt()
        ));
    }
}
