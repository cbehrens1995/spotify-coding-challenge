package org.cbehrens.spotifycodingchallenge.artist.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record ArtistSpotifyWrapperDto(
        @JsonProperty("artists")
        List<ArtistSpotifyDto> artistSpotifyDtos) {
}
