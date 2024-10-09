package com.tresende.catalog.admin.application.category.update;

import com.tresende.catalog.admin.domain.category.Category;

public record UpdateCategoryOutput(
        String id
) {

    public static UpdateCategoryOutput from(Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput from(String anId) {
        return new UpdateCategoryOutput(anId);
    }
}
