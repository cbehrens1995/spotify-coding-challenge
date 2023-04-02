package org.cbehrens.spotifycodingchallenge.album.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record AlbumsByArtistSpotifyDto(
        @JsonProperty("items")
        List<AlbumByArtistSpotifyDto> albumByArtistSpotifyDtos) {
}
