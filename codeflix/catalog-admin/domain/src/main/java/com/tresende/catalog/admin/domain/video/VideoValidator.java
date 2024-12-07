package com.tresende.catalog.admin.domain.video;

import com.tresende.catalog.admin.domain.validation.Error;
import com.tresende.catalog.admin.domain.validation.ValidationHandler;
import com.tresende.catalog.admin.domain.validation.Validator;

public class VideoValidator extends Validator {

    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4000;

    private final Video video;

    public VideoValidator(final Video aVideo, final ValidationHandler handler) {
        super(handler);
        this.video = aVideo;
    }

    @Override
    public void validate() {
        checkTitleConstraints();
        checkDescriptionConstraints();
        checkLaunchedAtConstraints();
        checkRatingConstraints();
    }

    private void checkTitleConstraints() {
        var title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }
        title = title.trim();
        if (title.isEmpty()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }
        if (title.length() > TITLE_MAX_LENGTH) {
            this.validationHandler().append(new Error("'title' must be between 1 and 255 characters"));
        }
    }

    private void checkDescriptionConstraints() {
        var description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }
        description = description.trim();
        if (description.isEmpty()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }
        if (description.length() > DESCRIPTION_MAX_LENGTH) {
            this.validationHandler().append(new Error("'description' must be between 1 and 4000 characters"));
        }
    }

    private void checkLaunchedAtConstraints() {
        var launchedAt = this.video.getLaunchedAt();
        if (launchedAt == null) {
            this.validationHandler().append(new Error("'launchedAt' should not be null"));
        }
    }

    private void checkRatingConstraints() {
        var rating = this.video.getRating();
        if (rating == null) {
            this.validationHandler().append(new Error("'rating' should not be null"));
        }
    }
}
