package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;
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

    public Artist createFromSpotify(ArtistSpotifyDto artistSpotifyDto) {
        String externalSpotifyUrl = artistSpotifyDto.externalUrls().spotify();
        Integer followersCount = artistSpotifyDto.followers().total();
        String imageUrl = artistSpotifyDto.images().stream()
                .map(Image::url)
                .findFirst().orElse(null);
        return createInternal(externalSpotifyUrl, followersCount, artistSpotifyDto.id(),
                imageUrl, artistSpotifyDto.name(), artistSpotifyDto.popularity(), artistSpotifyDto.uri(), Origin.SPOTIFY);
    }

    private Artist createInternal(String externalSpotifyUrl, Integer followersCount, String spotifyId, String imageUrl, String name, Integer popularity, String uri, Origin origin) {
        Artist artist = new Artist(externalSpotifyUrl, followersCount, spotifyId, imageUrl, name, popularity, uri, origin);
        return artistRepository.save(artist);
    }
}
