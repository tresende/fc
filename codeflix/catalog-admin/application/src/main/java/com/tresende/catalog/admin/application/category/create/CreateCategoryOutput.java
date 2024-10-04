package com.tresende.catalog.admin.application.category.create;

import com.tresende.catalog.admin.domain.category.Category;

public record CreateCategoryOutput(
        String id
) {

    public static CreateCategoryOutput from(Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
