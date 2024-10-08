package com.tresende.catalog.admin.application.category.update;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryID;

public record UpdateCategoryOutput(
        CategoryID id
) {

    public static UpdateCategoryOutput from(Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId());
    }
}
