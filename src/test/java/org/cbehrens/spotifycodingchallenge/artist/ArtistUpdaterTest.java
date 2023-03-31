package org.cbehrens.spotifycodingchallenge.artist;

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
    private SpotifyDtoValidator spotifyDtoValidator;

    private ArtistUpdater testee;

    @BeforeEach
    void init() {
        testee = new ArtistUpdater(spotifyDtoValidator);
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

        verify(spotifyDtoValidator).assertDtoHasNoSpotifyInformation(artistDto);
    }
}