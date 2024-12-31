package com.tresende.catalog.admin.infrastructure.video.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
class VideoCastMemberID implements Serializable {

    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "cast_member_id", nullable = false)
    private String CastMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(final String videoId, final String castMemberId) {
        this.videoId = videoId;
        CastMemberId = castMemberId;
    }

    public static VideoCastMemberID from(final String videoId, final String castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }

    @Override
    public boolean equals(final Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        final VideoCastMemberID that = (VideoCastMemberID) o;
        return Objects.equals(getVideoId(), that.getVideoId()) && Objects.equals(getCastMemberId(), that.getCastMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVideoId(), getCastMemberId());
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getCastMemberId() {
        return CastMemberId;
    }

    public void setCastMemberId(final String castMemberId) {
        CastMemberId = castMemberId;
    }
}
