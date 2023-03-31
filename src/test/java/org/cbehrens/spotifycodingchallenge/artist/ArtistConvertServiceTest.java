package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;
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
        var id = 187L;
        var artist = ArtistBuilder.artist(id)
                .withExternalSpotifyUrl(externalSpotifyUrl)
                .withFollowersCount(followersCount)
                .withSpotifyId(spotifyId)
                .withImageUrl(imageUrl)
                .withName(name)
                .withPopularity(popularity)
                .withUri(uri)
                .withOrigin(origin)
                .build();

        //when
        ArtistDto result = testee.toDto(artist);

        //then
        assertThat(result)
                .returns(id, AbstractDto::getId)
                .returns(externalSpotifyUrl, ArtistDto::getExternalSpotifyUrl)
                .returns(followersCount, ArtistDto::getFollowersCount)
                .returns(spotifyId, ArtistDto::getSpotifyId)
                .returns(imageUrl, ArtistDto::getImageUrl)
                .returns(popularity, ArtistDto::getPopularity)
                .returns(name, ArtistDto::getName)
                .returns(uri, ArtistDto::getUri)
                .returns(origin, ArtistDto::getOrigin);
    }
}