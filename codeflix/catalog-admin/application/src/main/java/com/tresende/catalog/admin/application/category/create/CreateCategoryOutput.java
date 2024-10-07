package com.tresende.catalog.admin.application.category.create;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryID;

public record CreateCategoryOutput(
        CategoryID id
) {

    public static CreateCategoryOutput from(Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId());
    }
}
