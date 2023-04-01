package org.cbehrens.spotifycodingchallenge.artist.spotify;

import java.util.ArrayList;
import java.util.List;

public class ArtistSpotifyWrapperDtoBuilder {

    private List<ArtistSpotifyDto> artists = new ArrayList<>();

    private ArtistSpotifyWrapperDtoBuilder() {
    }

    public static ArtistSpotifyWrapperDtoBuilder artistSpotifyWrapperDto() {
        return new ArtistSpotifyWrapperDtoBuilder();
    }

    public ArtistSpotifyWrapperDtoBuilder addArtistSpotifyDto(ArtistSpotifyDto artistSpotifyDto) {
        artists.add(artistSpotifyDto);
        return this;
    }

    public ArtistSpotifyWrapperDto build() {
        return new ArtistSpotifyWrapperDto(artists);
    }
}