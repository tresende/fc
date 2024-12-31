package com.tresende.catalog.admin.infrastructure.video.persistence;

import com.tresende.catalog.admin.domain.video.AudioVideoMedia;
import com.tresende.catalog.admin.domain.video.MediaStatus;
import jakarta.persistence.*;

@Table(name = "videos_video_media")
@Entity(name = "AudioVideoMedia")
public class AudioVideoMediaJpaEntity {
    @Id
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "encoded_path", nullable = false)
    private String encodedPath;

    @Column(name = "media_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private MediaStatus status;


    public AudioVideoMediaJpaEntity() {
    }

    public AudioVideoMediaJpaEntity(
            final String id,
            final String name,
            final String filePath,
            final String encodedPath,
            final MediaStatus status
    ) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia media) {
        return new AudioVideoMediaJpaEntity(
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status()
        );
    }

    public String getId() {
        return id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(final String filePath) {
        this.filePath = filePath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public void setEncodedPath(final String encodedPath) {
        this.encodedPath = encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public void setStatus(final MediaStatus status) {
        this.status = status;
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
                this.getId(),
                this.getName(),
                this.getFilePath(),
                this.getEncodedPath(),
                this.getStatus()
        );
    }
}
