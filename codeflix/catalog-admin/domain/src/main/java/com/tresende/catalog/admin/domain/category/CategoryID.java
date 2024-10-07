package com.tresende.catalog.admin.domain.category;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

public class CategoryID extends Identifier {
    private String value;

    private CategoryID(final String value) {
        this.value = Objects.requireNonNull(value, "'id' should not be null");
    }

    public static CategoryID unique() {
        return CategoryID.from(IdUtils.uuid());
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    public static CategoryID from(final UUID anId) {
        return new CategoryID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}