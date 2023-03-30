package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.cbehrens.spotifycodingchallenge.commons.PostgresContainer;
import org.cbehrens.spotifycodingchallenge.commons.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class ArtistApplicationIntegrationTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresContainer.getInstance();

    @Autowired
    private ArtistApplicationService testee;

    @DynamicPropertySource
    static void registerPostgresTestContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
    }

    @Test
    void thatNotExistingIdsThrowExceptions_Get_Delete() {
        //when /then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> testee.getById(1L));

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> testee.deleteById(1L));
    }

    /**
     * This test should test the data integrity
     * starting of by creating a new entity
     * read it form the db, updating a field
     * and then deleting it.
     */
    @Test
    void thatCRUDWorks() {
        // CREATE part
        var followersCount = 1337;
        var name = "Mamaduke";
        var popularity = 10;
        var artistDto = new ArtistDto(null, null, followersCount, null, null, name, popularity, null, null, false);

        ArtistDto result = testee.create(artistDto);

        Long id = result.id();
        assertThat(id).isNotNull();
        assertThat(result)
                .returns(followersCount, ArtistDto::followersCount)
                .returns(name, ArtistDto::name)
                .returns(popularity, ArtistDto::popularity)
                .returns(Origin.MANUAL, ArtistDto::origin);

        // READ part
        ArtistDto newArtistDto = testee.getById(id);
        assertThat(newArtistDto)
                .returns(id, ArtistDto::id)
                .returns(followersCount, ArtistDto::followersCount)
                .returns(name, ArtistDto::name)
                .returns(popularity, ArtistDto::popularity)
                .returns(Origin.MANUAL, ArtistDto::origin);

        // UPDATE part
        var updateArtistDto = new ArtistDto(id, null, followersCount + 1, null, null, name, popularity, null, null, false);

        ArtistDto updatedArtistDto = testee.update(updateArtistDto);
        assertThat(updatedArtistDto)
                .returns(id, ArtistDto::id)
                .returns(followersCount + 1, ArtistDto::followersCount)
                .returns(name, ArtistDto::name)
                .returns(popularity, ArtistDto::popularity)
                .returns(Origin.MANUAL, ArtistDto::origin);

        //DELETE part
        assertThatNoException().isThrownBy(() -> testee.deleteById(id));

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> testee.deleteById(1L));
    }
}
