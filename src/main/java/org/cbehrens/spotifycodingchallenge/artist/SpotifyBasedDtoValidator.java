package org.cbehrens.spotifycodingchallenge.artist;

import org.apache.commons.lang3.ObjectUtils;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyBasedDto;
import org.cbehrens.spotifycodingchallenge.commons.SpotifyInformationNotAllowedException;
import org.springframework.stereotype.Component;

@Component
public class SpotifyBasedDtoValidator {

    public <S extends AbstractSpotifyBasedDto> void assertDtoHasNoSpotifyInformation(S spotifyDto) {
        boolean allNull = ObjectUtils.allNull(spotifyDto.getExternalSpotifyUrl(), spotifyDto.getSpotifyId(), spotifyDto.getUri());
        if (!allNull) {
            throw new SpotifyInformationNotAllowedException("Spotify information are not allowed!");
        }
    }
}
