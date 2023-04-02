package org.cbehrens.spotifycodingchallenge.album.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.cbehrens.spotifycodingchallenge.album.AlbumType;
import org.cbehrens.spotifycodingchallenge.album.ReleaseDatePrecision;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.spotify.ExternalUrls;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;

import java.util.List;

public record AlbumSpotifyDto(
        @JsonProperty("album_type")
        AlbumType albumType,
        @JsonProperty("total_tracks")
        Integer trackCount,
        @JsonProperty("external_urls")
        ExternalUrls externalUrls,
        String id,
        List<Image> images,
        String name,
        @JsonProperty("release_date")
        String releaseDate,
        @JsonProperty("release_date_precision")
        ReleaseDatePrecision releaseDatePrecision,
        @JsonProperty("restrictions")
        Restriction restriction,
        String uri,
        @JsonProperty("copyrights")
        List<CopyrightSpotifyDto> copyrightSpotifyDtos,
        Integer popularity,
        @JsonProperty("artists")
        List<ArtistSpotifyDto> artistSpotifyDtos
) {
}
