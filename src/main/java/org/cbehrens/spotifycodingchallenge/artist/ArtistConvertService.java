package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArtistConvertService {

    public ArtistDto toDto(Artist artist) {
        return new ArtistDto(artist.getId(), artist.getExternalSpotifyUrl(), artist.getSpotifyId(), artist.getUri(),
                artist.getOrigin(), artist.isManuallyAdjusted(), artist.getFollowersCount(), artist.getImageUrl(),
                artist.getName(), artist.getPopularity());
    }

    public List<ArtistDto> toDtos(List<Artist> artists) {
        return artists.stream()
                .map(this::toDto)
                .toList();
    }
}
