package com.tresende.catalog.application.category.delete;

import com.tresende.catalog.application.UnitUseCase;
import com.tresende.catalog.domain.category.CategoryGateway;

import java.util.Objects;

public class DeleteCategoryUseCase extends UnitUseCase<String> {

    private final CategoryGateway categoryGateway;

    public DeleteCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(final String anId) {

        if (anId == null) {
            return;
        }

        categoryGateway.deleteById(anId);
    }
}
