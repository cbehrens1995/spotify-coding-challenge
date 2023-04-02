package org.cbehrens.spotifycodingchallenge.album.spotify;

import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightType;

public record CopyrightSpotifyDto(
        String text,
        CopyrightType type) {
}
