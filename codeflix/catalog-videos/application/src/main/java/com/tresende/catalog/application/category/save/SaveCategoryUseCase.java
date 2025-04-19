package com.tresende.catalog.application.category.save;

import com.tresende.catalog.application.UseCase;
import com.tresende.catalog.domain.category.Category;
import com.tresende.catalog.domain.category.CategoryGateway;
import com.tresende.catalog.domain.exceptions.NotificationException;
import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.handler.Notification;

import java.util.Objects;

public class SaveCategoryUseCase extends UseCase<Category, Category> {

    private final CategoryGateway categoryGateway;

    public SaveCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Category execute(final Category aCategory) {
        if (aCategory == null)
            throw NotificationException.with(new Error("category cant be null"));

        final var notification = Notification.create();
        aCategory.validate(notification);

        if (notification.hasError()) {
            throw NotificationException.with("", notification);
        }

        return categoryGateway.save(aCategory);
    }
}
