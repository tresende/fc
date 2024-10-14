package com.tresende.catalog.admin.application.genre.create;

import java.util.List;

public record CreateGenreCommand(
        String name,
        boolean isActive,
        List<String> categories
) {

    public static CreateGenreCommand with(
            final String name,
            final Boolean isActive,
            final List<String> categories
    ) {
        final var safeIsActive = isActive != null ? isActive : false;
        return new CreateGenreCommand(name, safeIsActive, categories);
    }
}
