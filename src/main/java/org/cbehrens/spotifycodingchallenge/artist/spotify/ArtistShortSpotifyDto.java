package org.cbehrens.spotifycodingchallenge.artist.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cbehrens.spotifycodingchallenge.commons.spotify.ExternalUrls;

public record ArtistShortSpotifyDto(
        @JsonProperty("external_urls")
        ExternalUrls externalUrls,
        String id,
        String name,
        String uri) {
}
