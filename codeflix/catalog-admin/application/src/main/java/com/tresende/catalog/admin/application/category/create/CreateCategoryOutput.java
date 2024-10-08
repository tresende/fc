package com.tresende.catalog.admin.application.category.create;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryID;

public record CreateCategoryOutput(
        String id
) {

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(final CategoryID anId) {
        return new CreateCategoryOutput(anId.getValue());
    }
}
