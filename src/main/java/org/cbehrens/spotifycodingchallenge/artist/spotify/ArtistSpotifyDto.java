package org.cbehrens.spotifycodingchallenge.artist.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

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
