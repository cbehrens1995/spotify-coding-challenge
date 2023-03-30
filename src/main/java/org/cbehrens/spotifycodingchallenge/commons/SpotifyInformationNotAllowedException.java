package org.cbehrens.spotifycodingchallenge.commons;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class SpotifyInformationNotAllowedException extends RuntimeException {

    public SpotifyInformationNotAllowedException() {
    }

    public SpotifyInformationNotAllowedException(String message) {
        super(message);
    }
}
