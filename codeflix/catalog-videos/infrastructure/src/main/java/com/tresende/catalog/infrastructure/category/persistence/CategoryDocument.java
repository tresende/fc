package com.tresende.catalog.infrastructure.category.persistence;

import com.tresende.catalog.domain.category.Category;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.Instant;

@Document(indexName = "categories")
public class CategoryDocument {
    @Id
    private String id;

    @MultiField(
            mainField = @Field(type = FieldType.Text, name = "name"),
            otherFields = @InnerField(type = FieldType.Keyword, suffix = "keyword")
    )
    private String name;

    @Field(type = FieldType.Text, name = "description")
    private String description;

    @Field(type = FieldType.Boolean, name = "active")
    private boolean active;

    @Field(type = FieldType.Date, name = "createdAt")
    private Instant createdAt;

    @Field(type = FieldType.Date, name = "updatedAt")
    private Instant updatedAt;

    @Field(type = FieldType.Date, name = "deletedAt")
    private Instant deletedAt;

    public CategoryDocument(final String id, final String name, final String description, final boolean active, final Instant createdAt, final Instant updatedAt, final Instant deletedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CategoryDocument from(final Category aCategory) {
        return new CategoryDocument(
                aCategory.id(),
                aCategory.name(),
                aCategory.description(),
                aCategory.active(),
                aCategory.createdAt(),
                aCategory.updatedAt(),
                aCategory.deletedAt()
        );
    }

    public Category toCategory() {
        return Category.with(
                id(),
                name(),
                description(),
                active(),
                createdAt(),
                updatedAt(),
                deletedAt()
        );
    }

    public String id() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String name() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String description() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public boolean active() {
        return active;
    }

    public void setActive(final boolean active) {
        this.active = active;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(final Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
