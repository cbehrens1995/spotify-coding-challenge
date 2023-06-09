package org.cbehrens.spotifycodingchallenge.artist.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cbehrens.spotifycodingchallenge.commons.spotify.ExternalUrls;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;

import java.util.List;

public record ArtistSpotifyDto(
        @JsonProperty(value = "external_urls")
        ExternalUrls externalUrls,
        Followers followers,
        String id,
        List<Image> images,
        String name,
        Integer popularity,
        String uri) {
}
