package com.tresende.catalog.admin.application;

import com.tresende.catalog.admin.domain.category.Category;

public class UseCase {
    public Category execute() {
        return Category.newCategory("a", "b", "false");
    }
}