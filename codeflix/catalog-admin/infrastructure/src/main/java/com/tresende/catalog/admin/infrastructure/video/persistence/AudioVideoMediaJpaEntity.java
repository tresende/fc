package com.tresende.catalog.admin.infrastructure.video.persistence;

import com.tresende.catalog.admin.domain.video.AudioVideoMedia;
import com.tresende.catalog.admin.domain.video.MediaStatus;
import jakarta.persistence.*;

@Table(name = "videos_video_media")
@Entity(name = "AudioVideoMedia")
public class AudioVideoMediaJpaEntity {
    @Id
    private String id;

    @Column(name = "checksum", nullable = false)
    private String checksum;

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
            final String checksum,
            final String name,
            final String filePath,
            final String encodedPath,
            final MediaStatus status
    ) {
        this.id = id;
        this.checksum = checksum;
        this.name = name;
        this.filePath = filePath;
        this.encodedPath = encodedPath;
        this.status = status;
    }

    public static AudioVideoMediaJpaEntity from(final AudioVideoMedia media) {
        return new AudioVideoMediaJpaEntity(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status()
        );
    }

    public String getChecksum() {
        return checksum;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getEncodedPath() {
        return encodedPath;
    }

    public MediaStatus getStatus() {
        return status;
    }

    public AudioVideoMedia toDomain() {
        return AudioVideoMedia.with(
                this.getId(),
                this.getChecksum(),
                this.getName(),
                this.getFilePath(),
                this.getEncodedPath(),
                this.getStatus()
        );
    }
}
