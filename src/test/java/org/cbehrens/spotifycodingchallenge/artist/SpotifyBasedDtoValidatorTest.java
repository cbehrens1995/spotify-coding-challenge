package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.SpotifyInformationNotAllowedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

@ExtendWith(MockitoExtension.class)
class SpotifyBasedDtoValidatorTest {

    private SpotifyBasedDtoValidator testee;

    @BeforeEach
    void init() {
        testee = new SpotifyBasedDtoValidator();
    }

    private static Stream<Arguments> parameterFor_thatCreateManuallyThrowsException_SpotifyInformationProvided() {
        return Stream.of(
                Arguments.of("externalSpotifyId", null, null),
                Arguments.of(null, "spotifyId", null),
                Arguments.of(null, null, "uri")
        );
    }

    @ParameterizedTest
    @MethodSource("parameterFor_thatCreateManuallyThrowsException_SpotifyInformationProvided")
    void thatAssertDtoHasNoSpotifyInformationThrowsException_SpotifyInformationProvided(String externalSpotifyUrl,
                                                                                        String spotifyId,
                                                                                        String uri) {
        //given
        var artistDto = ArtistDtoBuilder.artistDto(null)
                .withExternalSpotifyUrl(externalSpotifyUrl)
                .withSpotifyId(spotifyId)
                .withUri(uri)
                .build();

        //when /then
        assertThatExceptionOfType(SpotifyInformationNotAllowedException.class).isThrownBy(() -> testee.assertDtoHasNoSpotifyInformation(artistDto))
                .withMessage("Spotify information are not allowed!");
    }

    @Test
    void thatAssertDtoHasNoSpotifyInformationDoesNothing() {
        //given
        var artistDto = ArtistDtoBuilder.artistDto(null).build();

        //when /then
        assertThatNoException().isThrownBy(() -> testee.assertDtoHasNoSpotifyInformation(artistDto));
    }
}