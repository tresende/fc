package com.tresende.catalog.admin.infrastructure.video.persistence;

import com.tresende.catalog.admin.domain.castmember.CastMemberID;
import com.tresende.catalog.admin.domain.category.CategoryID;
import com.tresende.catalog.admin.domain.genre.GenreID;
import com.tresende.catalog.admin.domain.video.Rating;
import com.tresende.catalog.admin.domain.video.Video;
import com.tresende.catalog.admin.domain.video.VideoID;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.Year;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.EAGER;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "year_launched")
    private int yearLaunched;

    @Column(name = "opened")
    private boolean opened;

    @Column(name = "published")
    private boolean published;

    @Column(name = "rating")
    @Enumerated(EnumType.STRING)
    private Rating rating;

    @Column(name = "duration", precision = 2)
    private double duration;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "video_id")
    private AudioVideoMediaJpaEntity video;

    @OneToOne(cascade = ALL, fetch = EAGER, orphanRemoval = true)
    @JoinColumn(name = "trailer_id")
    private AudioVideoMediaJpaEntity trailer;

    @OneToMany(mappedBy = "video", cascade = ALL, orphanRemoval = true)
    private Set<VideoCategoryJpaEntity> categories;

    @OneToMany(mappedBy = "video", cascade = ALL, orphanRemoval = true)
    private Set<VideoGenreJpaEntity> genres;

    @OneToMany(mappedBy = "video", cascade = ALL, orphanRemoval = true)
    private Set<VideoCastMemberJpaEntity> castMembers;

    public VideoJpaEntity() {
        this.categories = new HashSet<>(3);
        this.genres = new HashSet<>(3);
        this.castMembers = new HashSet<>(3);
    }

    private VideoJpaEntity(
            final String id,
            final String title,
            final String description,
            final int yearLaunched,
            final boolean opened,
            final boolean published,
            final Rating rating,
            final double duration,
            final Instant createdAt,
            final Instant updatedAt,
            final AudioVideoMediaJpaEntity video,
            final AudioVideoMediaJpaEntity trailer
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.yearLaunched = yearLaunched;
        this.opened = opened;
        this.published = published;
        this.rating = rating;
        this.duration = duration;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.video = video;
        this.trailer = trailer;
        this.categories = new HashSet<>(3);
        this.genres = new HashSet<>(3);
        this.castMembers = new HashSet<>(3);
    }

    public static VideoJpaEntity from(final Video aVideo) {
        final var entity = new VideoJpaEntity(
                aVideo.getId().getValue(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getOpened(),
                aVideo.getPublished(),
                aVideo.getRating(),
                aVideo.getDuration(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getVideo().map(AudioVideoMediaJpaEntity::from).orElse(null),
                aVideo.getTrailer().map(AudioVideoMediaJpaEntity::from).orElse(null)
        );

        aVideo.getCategories()
                .forEach(entity::addCategory);

        aVideo.getGenres()
                .forEach(entity::addGenre);

        aVideo.getCastMembers()
                .forEach(entity::addCastMembers);


        return entity;
    }

    private void addCastMembers(CastMemberID castMemberID) {
        this.castMembers.add(VideoCastMemberJpaEntity.from(this, castMemberID));
    }

    private void addGenre(final GenreID genreID) {
        this.genres.add(VideoGenreJpaEntity.from(this, genreID));

    }

    public Set<VideoGenreJpaEntity> getGenres() {
        return genres;
    }

    public void setGenres(final Set<VideoGenreJpaEntity> genres) {
        this.genres = genres;
    }

    public void addCategory(final CategoryID categoryID) {
        this.categories.add(VideoCategoryJpaEntity.from(this, categoryID));
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public int getYearLaunched() {
        return yearLaunched;
    }

    public void setYearLaunched(final int yearLaunched) {
        this.yearLaunched = yearLaunched;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(final boolean opened) {
        this.opened = opened;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(final boolean published) {
        this.published = published;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(final Rating rating) {
        this.rating = rating;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(final double duration) {
        this.duration = duration;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public AudioVideoMediaJpaEntity getVideo() {
        return video;
    }

    public void setVideo(final AudioVideoMediaJpaEntity video) {
        this.video = video;
    }

    public AudioVideoMediaJpaEntity getTrailer() {
        return trailer;
    }

    public void setTrailer(final AudioVideoMediaJpaEntity trailer) {
        this.trailer = trailer;
    }

    public Set<VideoCategoryJpaEntity> getCategories() {
        return categories;
    }

    public void setCategories(final Set<VideoCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public Set<VideoCastMemberJpaEntity> getCastMembers() {
        return castMembers;
    }

    public void setCastMembers(final Set<VideoCastMemberJpaEntity> castMembers) {
        this.castMembers = castMembers;
    }

    public Video toAggregate() {
        return Video.with(
                VideoID.from(getId()),
                getTitle(),
                getDescription(),
                Year.of(getYearLaunched()),
                getDuration(),
                isOpened(),
                isPublished(),
                getRating(),
                getCreatedAt(),
                getUpdatedAt(),
                null,
                null,
                null,
                Optional.ofNullable(getTrailer())
                        .map(AudioVideoMediaJpaEntity::toDomain)
                        .orElse(null),
                Optional.ofNullable(getVideo())
                        .map(AudioVideoMediaJpaEntity::toDomain)
                        .orElse(null),
                getCategories().stream().map(it -> CategoryID.from(it.getId().getCategoryId()))
                        .collect(Collectors.toSet()),
                getGenres().stream().map(it -> GenreID.from(it.getId().getGenreId()))
                        .collect(Collectors.toSet()),
                getCastMembers().stream().map(it -> CastMemberID.from(it.getId().getCastMemberId()))
                        .collect(Collectors.toSet())
        );


    }
}
