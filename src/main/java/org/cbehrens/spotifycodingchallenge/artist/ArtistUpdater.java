package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ArtistUpdater {

    private final SpotifyDtoValidator spotifyDtoValidator;

    @Autowired
    public ArtistUpdater(SpotifyDtoValidator spotifyDtoValidator) {
        this.spotifyDtoValidator = spotifyDtoValidator;
    }

    public Artist update(Artist artist, ArtistDto artistDto) {
        spotifyDtoValidator.assertDtoHasNoSpotifyInformation(artistDto);
        boolean updatedOccurred = false;
        if (!Objects.equals(artist.getFollowersCount(), artistDto.getFollowersCount())) {
            artist.setFollowersCount(artistDto.getFollowersCount());
            updatedOccurred = true;
        }

        if (!Objects.equals(artist.getName(), artistDto.getName())) {
            artist.setName(artistDto.getName());
            updatedOccurred = true;
        }

        if (!Objects.equals(artist.getPopularity(), artistDto.getPopularity())) {
            artist.setPopularity(artistDto.getPopularity());
            updatedOccurred = true;
        }


        if (!Objects.equals(artist.getImageUrl(), artistDto.getImageUrl())) {
            artist.setImageUrl(artistDto.getImageUrl());
            updatedOccurred = true;
        }

        if (updatedOccurred) {
            artist.setManuallyAdjusted();
        }

        return artist;
    }
}
