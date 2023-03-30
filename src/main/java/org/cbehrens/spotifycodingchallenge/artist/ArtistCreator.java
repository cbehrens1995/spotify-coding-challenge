package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtistCreator {

    private final ArtistRepository artistRepository;
    private final ArtistDtoValidator artistDtoValidator;

    @Autowired
    public ArtistCreator(ArtistRepository artistRepository, ArtistDtoValidator artistDtoValidator) {
        this.artistRepository = artistRepository;
        this.artistDtoValidator = artistDtoValidator;
    }

    public Artist createManually(ArtistDto artistDto) {
        artistDtoValidator.assertDtoHasNoSpotifyInformation(artistDto);
        return createInternal(null, artistDto.followersCount(), null, artistDto.imageUrl(), artistDto.name(), artistDto.popularity(), null, Origin.MANUAL);
    }

    private Artist createInternal(String externalSpotifyUrl, Integer followersCount, String spotifyId, String imageUrl, String name, Integer popularity, String uri, Origin origin) {
        Artist artist = new Artist(externalSpotifyUrl, followersCount, spotifyId, imageUrl, name, popularity, uri, origin);
        return artistRepository.save(artist);
    }
}
