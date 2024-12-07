package com.tresende.catalog.admin.domain.video;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ImageMediaTest {

    @Test
    public void givenValidParams_whenCallsNewImage_shouldReturnInstance() {
        //given
        final var expectedChecksum = "name";
        final var expectedName = "banner.png";
        final var expectedLocation = "/iamages/ac";

        //when
        final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

        //then
        Assertions.assertNotNull(actualImage);
        Assertions.assertEquals(expectedChecksum, actualImage.checksum());
        Assertions.assertNotNull(expectedName, actualImage.name());
        Assertions.assertNotNull(expectedLocation, actualImage.location());
    }

    @Test
    public void givenTwoImagesWithSameLocationAndChecksum_whenCallsEquals_shouldReturnTrue() {
        //given
        final var expectedChecksum = "name";
        final var expectedLocation = "/iamages/ac";

        final var image2 = ImageMedia.with(expectedChecksum, "image2", expectedLocation);
        final var image1 = ImageMedia.with(expectedChecksum, "image1", expectedLocation);

        //then
        Assertions.assertEquals(image1, image2);
        Assertions.assertNotSame(image1, image2);
    }

    @Test
    public void givenInvalidParams_WhenCalls_shouldReturnError() {
        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with(null, "Random", "/abc")
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with("abc", null, "/abc")
        );

        Assertions.assertThrows(NullPointerException.class, () ->
                ImageMedia.with("abc", "Random", null)
        );

    }
}