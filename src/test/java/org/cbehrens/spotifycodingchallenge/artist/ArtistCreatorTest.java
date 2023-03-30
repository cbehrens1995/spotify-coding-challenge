package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistCreatorTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private ArtistDtoValidator artistDtoValidator;

    private ArtistCreator testee;

    @BeforeEach
    void init() {
        testee = new ArtistCreator(artistRepository, artistDtoValidator);
    }

    @Test
    void thatCreateManuallyWorks() {
        //given
        var followersCount = 1;
        var name = "Mamaduke";
        var popularity = 12;
        var imageUrl = "imageUrl";
        var artistDto = new ArtistDto(null, null, followersCount, null, imageUrl, name, popularity, null, null, false);

        when(artistRepository.save(any(Artist.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        Artist result = testee.createManually(artistDto);

        //then
        assertThat(result)
                .returns(name, Artist::getName)
                .returns(followersCount, Artist::getFollowersCount)
                .returns(popularity, Artist::getPopularity)
                .returns(Origin.MANUAL, Artist::getOrigin)
                .returns(imageUrl, Artist::getImageUrl)
                .returns(null, Artist::getExternalSpotifyUrl)
                .returns(null, Artist::getSpotifyId)
                .returns(null, Artist::getUri);
    }
}