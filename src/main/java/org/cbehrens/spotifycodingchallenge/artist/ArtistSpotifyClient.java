package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumsByArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyWrapperDto;
import org.cbehrens.spotifycodingchallenge.configuration.OAuthFeignConfiguration;
import org.springframework.cloud.openfeign.CollectionFormat;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        name = "artistSpotifyClient",
        url = "${artist.client.url}",
        configuration = OAuthFeignConfiguration.class)
public interface ArtistSpotifyClient {

    @GetMapping(value = "/{spotifyId}")
    ArtistSpotifyDto getArtist(@PathVariable("spotifyId") String id);

    @GetMapping
    @CollectionFormat(feign.CollectionFormat.CSV)
    ArtistSpotifyWrapperDto getArtists(@RequestParam("ids") List<String> ids);

    @GetMapping(value = "/{spotifyId}/albums")
    @CollectionFormat(feign.CollectionFormat.CSV)
    AlbumsByArtistSpotifyDto getAlbumsByArtist(@PathVariable("spotifyId") String id,
                                               @RequestParam("include_groups") List<String> includeGroups,
                                               @RequestParam("market") String market,
                                               @RequestParam("limit") Integer limit,
                                               @RequestParam("offset") Integer offset);

}
