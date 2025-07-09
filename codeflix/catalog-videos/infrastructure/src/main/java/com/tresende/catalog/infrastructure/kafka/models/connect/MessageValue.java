package com.tresende.catalog.infrastructure.kafka.models.connect;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MessageValue<T>(
        @JsonProperty("payload") ValuePayload<T> payload
) {
}
