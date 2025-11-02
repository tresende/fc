package com.tresende.catalog.infrastructure.kafka.models.connect;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Operation {
    CREATE("c"),
    UPDATE("u"),
    DELETE("d");

    private final String op;

    Operation(final String op) {
        this.op = op;
    }

    @JsonCreator
    public static Operation of(final String op) {
        for (Operation operation : Operation.values()) {
            if (operation.op.equals(op)) {
                return operation;
            }
        }
        throw new IllegalArgumentException("Unknown operation: " + op);
    }

    public static boolean isDelete(final Operation op) {
        return op == DELETE;
    }

    public boolean isDelete() {
        return this == DELETE;
    }

    @JsonValue
    public String op() {
        return op;
    }
}
