package com.tresende.catalog.admin.infrastructure.video;

import com.tresende.catalog.admin.IntegrationTest;
import com.tresende.catalog.admin.domain.Fixture;
import com.tresende.catalog.admin.domain.castmember.CastMemberGateway;
import com.tresende.catalog.admin.domain.category.CategoryGateway;
import com.tresende.catalog.admin.domain.genre.GenreGateway;
import com.tresende.catalog.admin.domain.video.AudioVideoMedia;
import com.tresende.catalog.admin.domain.video.ImageMedia;
import com.tresende.catalog.admin.domain.video.Video;
import com.tresende.catalog.admin.infrastructure.video.persistence.VideoRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;
import java.util.Set;

@IntegrationTest
class VideoGatewayTest {

    @Autowired
    private DefaultVideoGateway videoGateway;

    @Autowired
    private CastMemberGateway castMemberGateway;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(videoGateway);
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(videoRepository);
    }

    @Transactional
    @Test
    public void givenAValidVideo_whenCallsCreate_shouldPersistIt() {
        // given
        final var wesley = castMemberGateway.create(Fixture.CastMembers.wesley());
        final var aulas = categoryGateway.create(Fixture.Categories.aulas());
        final var tech = genreGateway.create(Fixture.Genres.tech());

        final var expectedTitle = Fixture.title();
        final var expectedDescription = Fixture.Videos.description();
        final var expectedLaunchYear = Year.of(Fixture.year());
        final var expectedDuration = Fixture.duration();
        final var expectedOpened = Fixture.bool();
        final var expectedPublished = Fixture.bool();
        final var expectedRating = Fixture.Videos.rating();
        final var expectedCategories = Set.of(aulas.getId());
        final var expectedGenres = Set.of(tech.getId());
        final var expectedMembers = Set.of(wesley.getId());

        final AudioVideoMedia expectedVideo =
                AudioVideoMedia.with("123", "video", "/media/video");

        final AudioVideoMedia expectedTrailer =
                AudioVideoMedia.with("123", "trailer", "/media/trailer");

        final ImageMedia expectedBanner =
                ImageMedia.with("123", "banner", "/media/banner");

        final ImageMedia expectedThumb =
                ImageMedia.with("123", "thumb", "/media/thumb");

        final ImageMedia expectedThumbHalf =
                ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");


        final var aVideo = Video.newVideo(
                        expectedTitle,
                        expectedDescription,
                        expectedLaunchYear,
                        expectedDuration,
                        expectedOpened,
                        expectedPublished,
                        expectedRating,
                        expectedCategories,
                        expectedGenres,
                        expectedMembers
                )
                .updateVideoMedia(expectedVideo)
                .updateTrailerMedia(expectedTrailer)
                .updateBannerMedia(expectedBanner)
                .updateThumbnailMedia(expectedThumb)
                .updateThumbnailHalfMedia(expectedThumbHalf);
        ;


        // when
        final var actualVideo = videoGateway.create(aVideo);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());

        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
        Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
        Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
        Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
        Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());

        final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

        Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
        Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
        Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
        Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
        Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
        Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
        Assertions.assertEquals(expectedRating, persistedVideo.getRating());
        Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
        Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
        Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
        Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
        Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
        Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
        Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
        Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    }

}