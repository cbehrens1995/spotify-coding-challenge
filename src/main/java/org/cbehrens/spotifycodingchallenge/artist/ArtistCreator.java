package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtistCreator {

    private final ArtistRepository artistRepository;
    private final SpotifyDtoValidator spotifyDtoValidator;

    @Autowired
    public ArtistCreator(ArtistRepository artistRepository, SpotifyDtoValidator spotifyDtoValidator) {
        this.artistRepository = artistRepository;
        this.spotifyDtoValidator = spotifyDtoValidator;
    }

    public Artist createManually(ArtistDto artistDto) {
        spotifyDtoValidator.assertDtoHasNoSpotifyInformation(artistDto);
        return createInternal(null, artistDto.getFollowersCount(), null, artistDto.getImageUrl(), artistDto.getName(), artistDto.getPopularity(), null, Origin.MANUAL);
    }

    private Artist createInternal(String externalSpotifyUrl, Integer followersCount, String spotifyId, String imageUrl, String name, Integer popularity, String uri, Origin origin) {
        Artist artist = new Artist(externalSpotifyUrl, followersCount, spotifyId, imageUrl, name, popularity, uri, origin);
        return artistRepository.save(artist);
    }
}
