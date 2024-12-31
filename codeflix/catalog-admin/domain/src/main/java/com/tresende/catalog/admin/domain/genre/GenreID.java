package com.tresende.catalog.admin.domain.genre;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.utils.IdUtils;

import java.util.Objects;

public class GenreID extends Identifier {
    private String value;

    private GenreID(final String value) {
        this.value = Objects.requireNonNull(value, "'id' should not be null");
    }

    public static GenreID unique() {
        return GenreID.from(IdUtils.uuid());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreID that = (GenreID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}