package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDtoBuilder;
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
    private SpotifyBasedDtoValidator spotifyBasedDtoValidator;

    private ArtistUpdater testee;

    @BeforeEach
    void init() {
        testee = new ArtistUpdater(spotifyBasedDtoValidator);
    }

    private static Stream<Arguments> parameterFor_thatUpdateManuallyWorks() {
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
    @MethodSource("parameterFor_thatUpdateManuallyWorks")
    void thatUpdateManuallyWorks(Integer followersCount,
                         String name,
                         Integer popularity,
                         String imageUrl,
                         boolean manuallyAdjusted) {
        //given
        var id = 187L;
        var artist = ArtistBuilder.artist(id)
                .withFollowersCount(1)
                .withImageUrl("imageUrl")
                .withName("name")
                .withPopularity(2)
                .build();

        var artistDto = ArtistDtoBuilder.artistDto(id)
                .withFollowersCount(followersCount)
                .withName(name)
                .withPopularity(popularity)
                .withImageUrl(imageUrl)
                .build();

        //when
        Artist result = testee.updateManually(artist, artistDto);

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

        verify(spotifyBasedDtoValidator).assertDtoHasNoSpotifyInformation(artistDto);
    }

    private static Stream<Arguments> parameterFor_thatUpdateFromSpotifyWorks() {
        return Stream.of(
                Arguments.of(1, "name", 2, "imageUrl", false),
                Arguments.of(2, "name", 2, "imageUrl", true),
                Arguments.of(null, "name", 2, "imageUrl", true),
                Arguments.of(1, "new_name", 2, "imageUrl", true),
                Arguments.of(1, "name", 3, "imageUrl", true),
                Arguments.of(1, "name", null, "imageUrl", true),
                Arguments.of(1, "name", 2, "new_imageUrl", true));
    }

    @ParameterizedTest
    @MethodSource("parameterFor_thatUpdateFromSpotifyWorks")
    void thatUpdateFromSpotifyWorks(Integer followersCount,
                                    String name,
                                    Integer popularity,
                                    String imageUrl) {
        //given
        var id = 187L;
        var artist = ArtistBuilder.artist(id)
                .withFollowersCount(1)
                .withImageUrl("imageUrl")
                .withName("name")
                .withPopularity(2)
                .build();

        var artistDto = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withFollowersCount(followersCount)
                .withName(name)
                .withPopularity(popularity)
                .addImageUrl(imageUrl)
                .build();

        //when
        testee.updateFromSpotify(artist, artistDto);

        //then
        assertThat(artist)
                .returns(followersCount, Artist::getFollowersCount)
                .returns(name, Artist::getName)
                .returns(popularity, Artist::getPopularity)
                .returns(imageUrl, Artist::getImageUrl)
                .returns(false, Artist::isManuallyAdjusted);
    }
}