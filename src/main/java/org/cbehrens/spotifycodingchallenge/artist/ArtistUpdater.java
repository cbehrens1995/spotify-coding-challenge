package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.SpotifyEntityUpdater;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtistUpdater extends SpotifyEntityUpdater<Artist> {

    private final SpotifyBasedDtoValidator spotifyBasedDtoValidator;

    @Autowired
    public ArtistUpdater(SpotifyBasedDtoValidator spotifyBasedDtoValidator) {
        this.spotifyBasedDtoValidator = spotifyBasedDtoValidator;
    }

    public Artist updateManually(Artist artist, ArtistDto artistDto) {
        spotifyBasedDtoValidator.assertDtoHasNoSpotifyInformation(artistDto);

        Integer followersCount = artistDto.getFollowersCount();
        String name = artistDto.getName();
        String imageUrl = artistDto.getImageUrl();
        Integer popularity = artistDto.getPopularity();

        boolean isBasicFieldUpdated = updateBasicField(artist, followersCount, name, imageUrl, popularity);
        if (isBasicFieldUpdated) {
            artist.setManuallyAdjusted();
        }

        return artist;
    }

    private boolean updateBasicField(Artist artist, Integer followersCount, String name, String imageUrl, Integer popularity) {
        boolean isFollowersCountUpdated = updateIfChanged(artist, Artist::getFollowersCount, Artist::setFollowersCount, followersCount);
        boolean isNameUpdated = updateIfChanged(artist, Artist::getName, Artist::setName, name);
        boolean isPopularityUpdated = updateIfChanged(artist, Artist::getPopularity, Artist::setPopularity, popularity);
        boolean isImageUrlUpdated = updateIfChanged(artist, Artist::getImageUrl, Artist::setImageUrl, imageUrl);

        return isFollowersCountUpdated || isNameUpdated || isPopularityUpdated || isImageUrlUpdated;
    }

    public void updateFromSpotify(Artist artist, ArtistSpotifyDto artistSpotifyDto) {
        Integer followersCount = artistSpotifyDto.followers().total();
        String name = artistSpotifyDto.name();
        String imageUrl = artistSpotifyDto.images().stream()
                .map(Image::url)
                .findFirst()
                .orElse(null);
        Integer popularity = artistSpotifyDto.popularity();

        updateBasicField(artist, followersCount, name, imageUrl, popularity);
    }
}
