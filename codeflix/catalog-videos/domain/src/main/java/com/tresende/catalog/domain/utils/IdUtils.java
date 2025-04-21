package com.tresende.catalog.domain.utils;

import java.util.UUID;

public final class IdUtils {

    private IdUtils() {
    }

    public static String uniqueId() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }
}
