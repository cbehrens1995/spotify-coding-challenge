package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ArtistSearchApplicationServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ArtistApplicationService artistApplicationService;
    @Autowired
    private ArtistSearchApplicationService testee;

    void initTestCustomers() {
        for (TestCustomer testCustomer : TestCustomer.values()) {
            createArtist(testCustomer);
        }
    }

    /**
     * The sleep call must happen since the test is faster than the elasticsearch container
     * As a result the system can outrun the search engine meaning no artists can be found.
     */
    @Test
    void thatSearchWorks() throws InterruptedException {
        //given
        initTestCustomers();
        Thread.sleep(1000);

        {
            //when
            List<ArtistDto> result = testee.search("band", 10);

            //then
            assertExpectedNamesAreFound(result, List.of(TestCustomer.TEST_CUSTOMER_3.getName(), TestCustomer.TEST_CUSTOMER_4.getName(), TestCustomer.TEST_CUSTOMER_5.getName()));
        }

        {
            //when
            List<ArtistDto> result = testee.search("Band", 10);

            //then
            assertExpectedNamesAreFound(result, List.of(TestCustomer.TEST_CUSTOMER_3.getName(), TestCustomer.TEST_CUSTOMER_4.getName(), TestCustomer.TEST_CUSTOMER_5.getName()));
        }

        {
            //when
            List<ArtistDto> result = testee.search("eim", 10);

            //then
            assertExpectedNamesAreFound(result, List.of(TestCustomer.TEST_CUSTOMER_1.getName(), TestCustomer.TEST_CUSTOMER_2.getName()));
        }

        {
            //when
            List<ArtistDto> result = testee.search("Nacht", 10);

            //then
            assertExpectedNamesAreFound(result, List.of(TestCustomer.TEST_CUSTOMER_1.getName()));
        }
    }

    private void assertExpectedNamesAreFound(List<ArtistDto> result, List<String> expectedNames) {
        List<String> resultedNames = result.stream()
                .map(ArtistDto::getName)
                .toList();
        assertThat(resultedNames).containsExactlyInAnyOrderElementsOf(expectedNames);
        assertThat(expectedNames).containsExactlyInAnyOrderElementsOf(resultedNames);
    }

    private enum TestCustomer {

        TEST_CUSTOMER_1("Max Nicolas Maria Nachtsheim"),
        TEST_CUSTOMER_2("Keimzeit"),
        TEST_CUSTOMER_3("Rasselbande"),
        TEST_CUSTOMER_4("187 Strassenbande"),
        TEST_CUSTOMER_5("Banditos");

        private final String artistName;

        TestCustomer(String artistName) {
            this.artistName = artistName;
        }

        public String getName() {
            return artistName;
        }
    }

    private void createArtist(TestCustomer testCustomer) {
        var artistDto = ArtistDtoBuilder.artistDto(null)
                .withName(testCustomer.getName())
                .build();

        artistApplicationService.create(artistDto);
    }
}