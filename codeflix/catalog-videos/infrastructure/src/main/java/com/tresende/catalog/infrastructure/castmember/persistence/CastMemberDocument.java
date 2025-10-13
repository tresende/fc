package com.tresende.catalog.infrastructure.castmember.persistence;

import com.tresende.catalog.domain.castmember.CastMember;
import com.tresende.catalog.domain.castmember.CastMemberType;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;

@Document(indexName = "cast_members")
public class CastMemberDocument {
    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "name"),
            otherFields = @InnerField(type = FieldType.Keyword, suffix = "keyword")
    )
    private final String name;
    @Field(type = FieldType.Keyword, name = "type")
    private final CastMemberType type;
    @Field(type = FieldType.Date, name = "created_at")
    private final Instant createdAt;
    @Field(type = FieldType.Date, name = "updated_at")
    private final Instant updatedAt;
    @Id
    private String id;

    public CastMemberDocument(final String id, final String name, final CastMemberType type, final Instant createdAt, final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static CastMemberDocument from(final CastMember castMember) {
        return new CastMemberDocument(
                castMember.id(),
                castMember.name(),
                castMember.type(),
                castMember.createdAt(),
                castMember.updatedAt()
        );
    }

    public String name() {
        return name;
    }

    public CastMemberType type() {
        return type;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public String id() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public CastMember toCastMember() {
        return CastMember.with(id(), name(), type(), createdAt(), updatedAt());
    }
}
