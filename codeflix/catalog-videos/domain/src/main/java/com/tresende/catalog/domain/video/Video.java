package com.tresende.catalog.domain.video;

import com.tresende.catalog.domain.validation.Error;
import com.tresende.catalog.domain.validation.ValidationHandler;
import com.tresende.catalog.domain.validation.handler.ThrowsValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Set;


public class Video {

    private final String id;
    private final String title;
    private final String description;
    private final Year launchedAt;
    private final double duration;
    private final Rating rating;

    private final boolean opened;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final String banner;
    private final String thumbnail;
    private final String thumbnailHalf;
    private final String trailer;
    private final String video;
    private final Set<String> categories;
    private final Set<String> genres;
    private final Set<String> castMembers;
    private boolean published;

    private Video(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String video,
            final String trailer,
            final String banner,
            final String thumbnail,
            final String thumbnailHalf,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.launchedAt = launchedAt != null ? Year.of(launchedAt) : null;
        this.duration = duration;
        this.rating = Rating.of(rating).orElse(null);
        this.opened = opened;
        this.published = published;
        this.createdAt = createdAt != null ? Instant.parse(createdAt) : null;
        this.updatedAt = updatedAt != null ? Instant.parse(updatedAt) : null;
        this.video = video;
        this.trailer = trailer;
        this.banner = banner;
        this.thumbnail = thumbnail;
        this.thumbnailHalf = thumbnailHalf;
        this.categories = categories != null ? categories : Set.of();
        this.genres = genres != null ? genres : Set.of();
        this.castMembers = castMembers != null ? castMembers : Set.of();

        validate(new ThrowsValidationHandler());

        if (video == null || video.isBlank()) {
            this.published = false;
        }

        if (trailer == null || trailer.isBlank()) {
            this.published = false;
        }

        if (banner == null || banner.isBlank()) {
            this.published = false;
        }

        if (thumbnail == null || thumbnail.isBlank()) {
            this.published = false;
        }

        if (thumbnailHalf == null || thumbnailHalf.isBlank()) {
            this.published = false;
        }
    }

    public static Video with(
            final String id,
            final String title,
            final String description,
            final Integer launchedAt,
            final double duration,
            final String rating,
            final boolean opened,
            final boolean published,
            final String createdAt,
            final String updatedAt,
            final String video,
            final String trailer,
            final String banner,
            final String thumbnail,
            final String thumbnailHalf,
            final Set<String> categories,
            final Set<String> castMembers,
            final Set<String> genres
    ) {
        return new Video(
                id,
                title,
                description,
                launchedAt,
                duration,
                rating,
                opened,
                published,
                createdAt,
                updatedAt,
                video,
                trailer,
                banner,
                thumbnail,
                thumbnailHalf,
                categories,
                castMembers,
                genres
        );
    }

    public static Video with(final Video video) {
        return with(
                video.id(),
                video.title(),
                video.description(),
                video.launchedAt().getValue(),
                video.duration(),
                video.rating().getName(),
                video.opened(),
                video.published(),
                video.createdAt().toString(),
                video.updatedAt().toString(),
                video.video(),
                video.trailer(),
                video.banner(),
                video.thumbnail(),
                video.thumbnailHalf(),
                video.categories(),
                video.castMembers(),
                video.genres()
        );
    }

    public void validate(final ValidationHandler handler) {
        if (id == null || id.isBlank()) {
            handler.append(new Error("'id' should not be empty"));
        }

        if (title == null || title.isBlank()) {
            handler.append(new Error("'title' should not be empty"));
        }

        if (launchedAt == null) {
            handler.append(new Error("'launchedAt' should not be empty"));
        }

        if (rating == null) {
            handler.append(new Error("'rating' should not be empty"));
        }

        if (createdAt == null) {
            handler.append(new Error("'createdAt' should not be empty"));
        }

        if (updatedAt == null) {
            handler.append(new Error("'updatedAt' should not be empty"));
        }
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String description() {
        return description;
    }

    public Year launchedAt() {
        return launchedAt;
    }

    public double duration() {
        return duration;
    }

    public Rating rating() {
        return rating;
    }

    public boolean opened() {
        return opened;
    }

    public boolean published() {
        return published;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public String banner() {
        return banner;
    }

    public String thumbnail() {
        return thumbnail;
    }

    public String thumbnailHalf() {
        return thumbnailHalf;
    }

    public String trailer() {
        return trailer;
    }

    public String video() {
        return video;
    }

    public Set<String> categories() {
        return categories;
    }

    public Set<String> genres() {
        return genres;
    }

    public Set<String> castMembers() {
        return castMembers;
    }
}

