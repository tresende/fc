package com.tresende.catalog.domain.category;

import com.tresende.catalog.domain.UnitTest;
import com.tresende.catalog.domain.exceptions.DomainException;
import com.tresende.catalog.domain.utils.IdUtils;
import com.tresende.catalog.domain.utils.InstantUtils;
import com.tresende.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CategoryTest extends UnitTest {

    @Test
    public void givenAValidParams_whenCallWith_thenInstantiateACategory() {
        //given
        final var expectedID = IdUtils.uuid();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        //when
        final var actualCategory =
                Category.with(expectedID, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        //then
        Assertions.assertNotNull(actualCategory);
        Assertions.assertEquals(expectedID, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(expectedDates, actualCategory.createdAt());
        Assertions.assertEquals(expectedDates, actualCategory.updatedAt());
        Assertions.assertEquals(expectedDates, actualCategory.deletedAt());
    }

    @Test
    public void givenAValidParams_whenCallWithCategory_thenInstantiateACategory() {
        //given
        final var expectedID = IdUtils.uuid();
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();

        final var aCategory =
                Category.with(expectedID, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);

        //when
        final var actualCategory = Category.with(aCategory);

        //then
        Assertions.assertEquals(aCategory.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertTrue(aCategory.active());
        Assertions.assertEquals(expectedDates, actualCategory.createdAt());
        Assertions.assertEquals(expectedDates, actualCategory.updatedAt());
        Assertions.assertEquals(expectedDates, actualCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = IdUtils.uuid();
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        //when
        final var actualCategory =
                Category.with(expectedID, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = IdUtils.uuid();
        final String expectedName = " ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        //when
        final var actualCategory =
                Category.with(expectedID, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

    @Test
    public void givenAnInvalidNullId_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        //given
        final String expectedID = null;
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        //when
        final var actualCategory =
                Category.with(expectedID, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().getFirst().message());
    }

    @Test
    public void givenAnInvalidEmptyId_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        //given
        final var expectedID = " ";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedDates = InstantUtils.now();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'id' should not be empty";

        //when
        final var actualCategory =
                Category.with(expectedID, expectedName, expectedDescription, expectedIsActive, expectedDates, expectedDates, expectedDates);


        final var actualException =
                Assertions.assertThrows(DomainException.class, () -> actualCategory.validate(new ThrowsValidationHandler()));

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }
}