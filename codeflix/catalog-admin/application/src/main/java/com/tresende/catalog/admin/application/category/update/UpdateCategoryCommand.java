package com.tresende.catalog.admin.application.category.update;

public record UpdateCategoryCommand(
        String id,
        String name,
        String description,
        Boolean isActive
) {

    public static UpdateCategoryCommand with(
            final String anId,
            final String aName,
            final String aDescription,
            final Boolean anIsActive
    ) {
        return new UpdateCategoryCommand(anId, aName, aDescription, anIsActive);
    }
}
