package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ArtistConvertServiceTest {

    private ArtistConvertService testee;

    @BeforeEach
    void init() {
        testee = new ArtistConvertService();
    }

    @Test
    void thatToDtoWorks() {
        //given
        var externalSpotifyUrl = "externalSpotifyUrl";
        var followersCount = 2;
        var spotifyId = "spotifyId";
        var imageUrl = "localhost:1887";
        Integer popularity = null;
        var name = "name";
        var uri = "uri";
        var origin = Origin.MANUAL;
        var artist = new Artist(externalSpotifyUrl, followersCount, spotifyId, imageUrl, name, popularity, uri, origin);

        //when
        ArtistDto result = testee.toDto(artist);

        //then
        assertThat(result)
                .returns(externalSpotifyUrl, ArtistDto::externalSpotifyUrl)
                .returns(followersCount, ArtistDto::followersCount)
                .returns(spotifyId, ArtistDto::spotifyId)
                .returns(imageUrl, ArtistDto::imageUrl)
                .returns(popularity, ArtistDto::popularity)
                .returns(name, ArtistDto::name)
                .returns(uri, ArtistDto::uri)
                .returns(origin, ArtistDto::origin);
    }
}