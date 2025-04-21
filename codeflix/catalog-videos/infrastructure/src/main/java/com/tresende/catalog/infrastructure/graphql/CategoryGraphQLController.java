package com.tresende.catalog.infrastructure.graphql;

import com.tresende.catalog.application.category.list.ListCategoryOutput;
import com.tresende.catalog.application.category.list.ListCategoryUseCase;
import com.tresende.catalog.application.category.save.SaveCategoryUseCase;
import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategorySearchQuery;
import com.tresende.catalog.infrastructure.category.models.GqlCategoryInput;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Objects;

@Controller
public class CategoryGraphQLController {

    private final ListCategoryUseCase listCategoryUseCase;
    private final SaveCategoryUseCase saveCategoryUseCase;

    public CategoryGraphQLController(
            final ListCategoryUseCase listCategoryUseCase,
            final SaveCategoryUseCase saveCategoryUseCase
    ) {
        this.listCategoryUseCase = Objects.requireNonNull(listCategoryUseCase);
        this.saveCategoryUseCase = Objects.requireNonNull(saveCategoryUseCase);
    }

    @MutationMapping
    public Category saveCategory(@Argument final GqlCategoryInput input) {
        return this.saveCategoryUseCase.execute(input.toCategory());
    }

    @QueryMapping
    public List<ListCategoryOutput> categories(
            @Argument final String search,
            @Argument final int page,
            @Argument final int perPage,
            @Argument final String sort,
            @Argument final String direction
    ) {
        final var aQuery = new CategorySearchQuery(page, perPage, search, sort, direction);
        return listCategoryUseCase.execute(aQuery).data();
    }
}
