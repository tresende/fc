package com.tresende.catalog.admin.application.category.create;

import com.tresende.catalog.admin.domain.category.Category;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
        final var name = aCommand.name();
        final var description = aCommand.description();
        final var isActive = aCommand.isActive();
        final var aCategory = Category.newCategory(name, description, isActive);
        final var notification = Notification.create();
        aCategory.validate(notification);

        return notification.hasError() ?
                Left(notification) :
                create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(Category aCategory) {
        return Try(() -> categoryGateway.create(aCategory))
                .toEither()
                .bimap(
                        Notification::create,
                        CreateCategoryOutput::from
                );
//                .map()
//                .mapLeft();
    }
}
