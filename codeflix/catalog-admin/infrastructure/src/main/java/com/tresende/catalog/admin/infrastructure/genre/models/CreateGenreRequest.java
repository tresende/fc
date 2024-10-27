package com.tresende.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.List;

public record CreateGenreRequest(
        @JsonProperty("name") String name,
        @JsonProperty("categories") List<String> categories,
        @JsonProperty("is_active") Boolean active
) {

    public List<String> categories() {
        return this.categories != null ? categories : Collections.emptyList();
    }

    public boolean isActive() {
        return this.active != null ? active : true;
    }
}
