package org.cbehrens.spotifycodingchallenge.artist;

import org.apache.commons.lang3.ObjectUtils;
import org.cbehrens.spotifycodingchallenge.commons.SpotifyInformationNotAllowedException;
import org.springframework.stereotype.Component;

@Component
public class ArtistDtoValidator {

    public void assertDtoHasNoSpotifyInformation(ArtistDto artistDto) {
        boolean allNull = ObjectUtils.allNull(artistDto.externalSpotifyUrl(), artistDto.spotifyId(), artistDto.uri());
        if (!allNull) {
            throw new SpotifyInformationNotAllowedException("Spotify information are not allowed when creating manually an artist!");
        }
    }
}
