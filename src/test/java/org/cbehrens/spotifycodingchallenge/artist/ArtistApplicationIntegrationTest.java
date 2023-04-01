package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractIntegrationTest;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.cbehrens.spotifycodingchallenge.commons.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class ArtistApplicationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private ArtistApplicationService testee;

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
        var artistDto = ArtistDtoBuilder.artistDto(null)
                .withFollowersCount(followersCount)
                .withName(name)
                .withPopularity(popularity)
                .build();

        ArtistDto result = testee.create(artistDto);

        Long id = result.getId();
        assertThat(id).isNotNull();
        assertThat(result)
                .returns(followersCount, ArtistDto::getFollowersCount)
                .returns(name, ArtistDto::getName)
                .returns(popularity, ArtistDto::getPopularity)
                .returns(Origin.MANUAL, ArtistDto::getOrigin);

        // READ part
        ArtistDto newArtistDto = testee.getById(id);
        assertThat(newArtistDto)
                .returns(id, ArtistDto::getId)
                .returns(followersCount, ArtistDto::getFollowersCount)
                .returns(name, ArtistDto::getName)
                .returns(popularity, ArtistDto::getPopularity)
                .returns(Origin.MANUAL, ArtistDto::getOrigin);

        // UPDATE part
        var updateArtistDto = ArtistDtoBuilder.artistDto(id)
                .withFollowersCount(followersCount + 1)
                .withName(name)
                .withPopularity(popularity)
                .build();

        ArtistDto updatedArtistDto = testee.update(updateArtistDto);
        assertThat(updatedArtistDto)
                .returns(id, ArtistDto::getId)
                .returns(followersCount + 1, ArtistDto::getFollowersCount)
                .returns(name, ArtistDto::getName)
                .returns(popularity, ArtistDto::getPopularity)
                .returns(Origin.MANUAL, ArtistDto::getOrigin);

        //DELETE part
        assertThatNoException().isThrownBy(() -> testee.deleteById(id));

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> testee.deleteById(1L));
    }
}
