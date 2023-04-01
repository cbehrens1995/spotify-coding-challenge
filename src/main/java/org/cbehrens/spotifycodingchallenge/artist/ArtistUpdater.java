package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.SpotifyEntityUpdater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtistUpdater extends SpotifyEntityUpdater<Artist> {

    private final SpotifyDtoValidator spotifyDtoValidator;

    @Autowired
    public ArtistUpdater(SpotifyDtoValidator spotifyDtoValidator) {
        this.spotifyDtoValidator = spotifyDtoValidator;
    }

    public Artist update(Artist artist, ArtistDto artistDto) {
        spotifyDtoValidator.assertDtoHasNoSpotifyInformation(artistDto);

        boolean isFollowersCountUpdated = updateIfChanged(artist, Artist::getFollowersCount, Artist::setFollowersCount, artistDto.getFollowersCount());
        boolean isNameUpdated = updateIfChanged(artist, Artist::getName, Artist::setName, artistDto.getName());
        boolean isPopularityUpdated = updateIfChanged(artist, Artist::getPopularity, Artist::setPopularity, artistDto.getPopularity());
        boolean isImageUrlUpdated = updateIfChanged(artist, Artist::getImageUrl, Artist::setImageUrl, artistDto.getImageUrl());

        if (isFollowersCountUpdated || isNameUpdated || isPopularityUpdated || isImageUrlUpdated) {
            artist.setManuallyAdjusted();
        }

        return artist;
    }
}
