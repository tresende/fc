package com.tresende.catalog.admin.application.genre.update;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.exceptions.DomainException;
import com.tresende.catalog.admin.domain.exceptions.NotFoundException;
import com.tresende.catalog.admin.domain.exceptions.NotificationException;
import com.tresende.catalog.admin.domain.genre.Genre;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.validation.Error;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;
import com.tresende.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpateGenreUseCase {

    private final GenreGateway genreGateway;
    private final CategoryGateway categoryGateway;

    public DefaultUpdateGenreUseCase(
            final GenreGateway genreGateway,
            final CategoryGateway categoryGateway
    ) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = aCommand.id();
        final var aName = aCommand.name();
        final var isActive = aCommand.isActive();
        final var categories = toCategoryId(aCommand.categories());

        final var aGenre = genreGateway.findById(anId).orElseThrow(this.notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));
        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(aCommand.id()), notification
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final var notification = Notification.create();
        if (ids.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }


    private List<CategoryID> toCategoryId(
            final List<String> categories
    ) {
        return categories.stream().map(CategoryID::from).toList();
    }

    private Supplier<DomainException> notFound(final Identifier anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }
}
