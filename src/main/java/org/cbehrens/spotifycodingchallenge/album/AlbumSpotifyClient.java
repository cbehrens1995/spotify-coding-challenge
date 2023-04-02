package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumSpotifyDto;
import org.cbehrens.spotifycodingchallenge.configuration.OAuthFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "albumSpotifyClient",
        url = "${album.client.url}",
        configuration = OAuthFeignConfiguration.class)
public interface AlbumSpotifyClient {

    @GetMapping(value = "/{spotifyId}")
    AlbumSpotifyDto getAlbum(@PathVariable("spotifyId") String id);

}
