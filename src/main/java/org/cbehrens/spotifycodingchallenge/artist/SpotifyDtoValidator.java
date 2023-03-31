package org.cbehrens.spotifycodingchallenge.artist;

import org.apache.commons.lang3.ObjectUtils;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.SpotifyInformationNotAllowedException;
import org.springframework.stereotype.Component;

@Component
public class SpotifyDtoValidator {

    public <S extends AbstractSpotifyDto> void assertDtoHasNoSpotifyInformation(S spotifyDto) {
        boolean allNull = ObjectUtils.allNull(spotifyDto.getExternalSpotifyUrl(), spotifyDto.getSpotifyId(), spotifyDto.getUri());
        if (!allNull) {
            throw new SpotifyInformationNotAllowedException("Spotify information are not allowed when creating manually an artist!");
        }
    }
}
