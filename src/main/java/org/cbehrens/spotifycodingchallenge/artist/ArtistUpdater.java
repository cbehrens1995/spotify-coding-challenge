package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ArtistUpdater {

    private final ArtistDtoValidator artistDtoValidator;

    @Autowired
    public ArtistUpdater(ArtistDtoValidator artistDtoValidator) {
        this.artistDtoValidator = artistDtoValidator;
    }

    public Artist update(Artist artist, ArtistDto artistDto) {
        artistDtoValidator.assertDtoHasNoSpotifyInformation(artistDto);
        boolean updatedOccurred = false;
        if (!Objects.equals(artist.getFollowersCount(), artistDto.followersCount())) {
            artist.setFollowersCount(artistDto.followersCount());
            updatedOccurred = true;
        }

        if (!Objects.equals(artist.getName(), artistDto.name())) {
            artist.setName(artistDto.name());
            updatedOccurred = true;
        }

        if (!Objects.equals(artist.getPopularity(), artistDto.popularity())) {
            artist.setPopularity(artistDto.popularity());
            updatedOccurred = true;
        }


        if (!Objects.equals(artist.getImageUrl(), artistDto.imageUrl())) {
            artist.setImageUrl(artistDto.imageUrl());
            updatedOccurred = true;
        }

        if (updatedOccurred) {
            artist.setManuallyAdjusted();
        }

        return artist;
    }
}
