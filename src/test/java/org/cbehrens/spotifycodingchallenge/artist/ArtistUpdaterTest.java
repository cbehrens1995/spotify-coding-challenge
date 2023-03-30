package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ArtistUpdaterTest {

    @Mock
    private ArtistDtoValidator artistDtoValidator;

    private ArtistUpdater testee;

    @BeforeEach
    void init() {
        testee = new ArtistUpdater(artistDtoValidator);
    }

    private static Stream<Arguments> parameterFor_thatUpdateWorks() {
        return Stream.of(
                Arguments.of(1, "name", 2, "imageUrl", false),
                Arguments.of(2, "name", 2, "imageUrl", true),
                Arguments.of(null, "name", 2, "imageUrl", true),
                Arguments.of(1, "new_name", 2, "imageUrl", true),
                Arguments.of(1, "name", 3, "imageUrl", true),
                Arguments.of(1, "name", null, "imageUrl", true),
                Arguments.of(1, "name", 2, "new_imageUrl", true),
                Arguments.of(1, "name", 2, null, true));
    }

    @ParameterizedTest
    @MethodSource("parameterFor_thatUpdateWorks")
    void thatUpdateWorks(Integer followersCount,
                         String name,
                         Integer popularity,
                         String imageUrl,
                         boolean manuallyAdjusted) {
        //given
        var artist = new Artist(null, 1, null, "imageUrl", "name", 2, null, Origin.MANUAL);
        var artistDto = new ArtistDto(null, null, followersCount, null, imageUrl, name, popularity, null, Origin.MANUAL, false);

        //when
        Artist result = testee.update(artist, artistDto);

        //then
        assertThat(result)
                .returns(followersCount, Artist::getFollowersCount)
                .returns(name, Artist::getName)
                .returns(popularity, Artist::getPopularity)
                .returns(imageUrl, Artist::getImageUrl)
                .returns(manuallyAdjusted, Artist::isManuallyAdjusted)
                .returns(null, Artist::getExternalSpotifyUrl)
                .returns(null, Artist::getSpotifyId)
                .returns(null, Artist::getUri);

        verify(artistDtoValidator).assertDtoHasNoSpotifyInformation(artistDto);
    }
}