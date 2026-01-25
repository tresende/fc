package com.tresende.catalog.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GenreEvent(
        @JsonProperty("id") String id
) {
}