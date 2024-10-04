package com.tresende.catalog.admin.application.category.update;

import com.tresende.catalog.admin.application.UseCase;
import com.tresende.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}