package com.tresende.catalog.admin.application.category.create;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class CreateCategoryUseCase extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}