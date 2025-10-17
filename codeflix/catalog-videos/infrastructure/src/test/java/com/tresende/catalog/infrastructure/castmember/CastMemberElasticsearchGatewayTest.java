package com.tresende.catalog.infrastructure.castmember;

import com.tresende.catalog.AbstractElasticsearchTest;
import com.tresende.catalog.domain.Fixture;
import com.tresende.catalog.domain.castmember.CastMemberSearchQuery;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberDocument;
import com.tresende.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

public class CastMemberElasticsearchGatewayTest extends AbstractElasticsearchTest {
    @Autowired
    private CastMemberElasticsearchGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    public void testInjection() {
        Assertions.assertNotNull(castMemberGateway);
        Assertions.assertNotNull(castMemberRepository);
    }

    @Test
    public void givenValidCastMember_whenCallsSave_shouldPersistIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        // when
        final var actualOutput = this.castMemberGateway.save(gabriel);

        // then
        Assertions.assertEquals(gabriel, actualOutput);

        final var actualCastMember = this.castMemberRepository.findById(gabriel.id()).get();
        Assertions.assertEquals(gabriel.id(), actualCastMember.id());
        Assertions.assertEquals(gabriel.name(), actualCastMember.name());
        Assertions.assertEquals(gabriel.type(), actualCastMember.type());
        Assertions.assertEquals(gabriel.createdAt(), actualCastMember.createdAt());
        Assertions.assertEquals(gabriel.updatedAt(), actualCastMember.updatedAt());
    }

    @Test
    public void givenValidId_whenCallsDeleteById_shouldDeleteIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        this.castMemberRepository.save(CastMemberDocument.from(gabriel));

        final var expectedId = gabriel.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        // when
        this.castMemberGateway.deleteById(expectedId);

        // then
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteById_shouldBeOk() {
        // given
        final var expectedId = "any";

        // when/then
        Assertions.assertDoesNotThrow(() -> this.castMemberGateway.deleteById(expectedId));
    }


    @Test
    public void givenValidId_whenCallsFindById_shouldRetrieveIt() {
        // given
        final var gabriel = Fixture.CastMembers.gabriel();

        this.castMemberRepository.save(CastMemberDocument.from(gabriel));

        final var expectedId = gabriel.id();
        Assertions.assertTrue(this.castMemberRepository.existsById(expectedId));

        // when
        final var actualOutput = this.castMemberGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(gabriel.id(), actualOutput.id());
        Assertions.assertEquals(gabriel.name(), actualOutput.name());
        Assertions.assertEquals(gabriel.type(), actualOutput.type());
        Assertions.assertEquals(gabriel.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(gabriel.updatedAt(), actualOutput.updatedAt());
    }

    @Test
    public void givenInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // given
        final var expectedId = "any";

        // when
        final var actualOutput = this.castMemberGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualOutput.isEmpty());
    }


    @Test
    public void givenEmptyCategories_whenCallsFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedTotal, actualOutput.data().size());
    }

    @ParameterizedTest
    @CsvSource({
            "gab,0,10,1,1,Gabriel FullCycle",
            "leo,0,10,1,1,Leonan FullCycle"
    })
    public void givenValidTerm_whenCallsFindAll_shouldReturnElementsFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockCastMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,3,3,Gabriel FullCycle",
            "name,desc,0,10,3,3,Wesley FullCycle",
            "created_at,asc,0,10,3,3,Gabriel FullCycle",
            "created_at,desc,0,10,3,3,Leonan FullCycle",
    })
    public void givenValidSortAndDirection_whenCallsFindAll_shouldReturnElementsSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockCastMembers();

        final var expectedTerms = "";

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());
        Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,1,1,3,Gabriel FullCycle",
            "1,1,1,3,Leonan FullCycle",
            "2,1,1,3,Wesley FullCycle",
            "3,1,0,3,",
    })
    public void givenValidPage_whenCallsFindAll_shouldReturnElementsPaged(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // given
        mockCastMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new CastMemberSearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualOutput = this.castMemberGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.meta().currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.meta().perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.meta().total());
        Assertions.assertEquals(expectedItemsCount, actualOutput.data().size());

        if (StringUtils.isNotEmpty(expectedName)) {
            Assertions.assertEquals(expectedName, actualOutput.data().get(0).name());
        }
    }

    private void mockCastMembers() {
        this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.gabriel()));
        this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.wesley()));
        this.castMemberRepository.save(CastMemberDocument.from(Fixture.CastMembers.leonan()));
    }
}