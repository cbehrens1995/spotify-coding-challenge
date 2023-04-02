package org.cbehrens.spotifycodingchallenge.album.spotify;

import java.util.ArrayList;
import java.util.List;

public class AlbumsByArtistSpotifyDtoBuilder {

    private List<AlbumByArtistSpotifyDto> albumByArtistSpotifyDtos = new ArrayList<>();

    private AlbumsByArtistSpotifyDtoBuilder() {
    }

    public static AlbumsByArtistSpotifyDtoBuilder albumsByArtistSpotifyDto() {
        return new AlbumsByArtistSpotifyDtoBuilder();
    }

    public AlbumsByArtistSpotifyDtoBuilder addAlbumByArtistSpotifyDto(AlbumByArtistSpotifyDto albumByArtistSpotifyDto) {
        albumByArtistSpotifyDtos.add(albumByArtistSpotifyDto);
        return this;
    }

    public AlbumsByArtistSpotifyDto build() {
        return new AlbumsByArtistSpotifyDto(albumByArtistSpotifyDtos);
    }

}