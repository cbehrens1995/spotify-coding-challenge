package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.stereotype.Component;

@Component
public class ArtistConvertService {

    public ArtistDto toDto(Artist artist) {
        return new ArtistDto(artist.getId(), artist.getExternalSpotifyUrl(), artist.getFollowersCount(), artist.getSpotifyId(),
                artist.getImageUrl(), artist.getName(), artist.getPopularity(), artist.getUri(), artist.getOrigin(), artist.isManuallyAdjusted());
    }
}
