package com.tresende.catalog.admin.domain.castmember;

import com.tresende.catalog.admin.domain.Identifier;
import com.tresende.catalog.admin.domain.utils.IdUtils;

import java.util.Objects;

public class CastMemberID extends Identifier {
    private String value;

    private CastMemberID(final String value) {
        this.value = Objects.requireNonNull(value, "'id' should not be null");
    }

    public static CastMemberID unique() {
        return CastMemberID.from(IdUtils.uuid());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}