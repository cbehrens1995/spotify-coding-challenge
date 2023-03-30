package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;

public record ArtistDto(
        Long id,
        String externalSpotifyUrl,
        Integer followersCount,
        String spotifyId,
        String imageUrl,
        String name,
        Integer popularity,
        String uri,
        Origin origin,
        boolean manuallyAdjusted) {
}
