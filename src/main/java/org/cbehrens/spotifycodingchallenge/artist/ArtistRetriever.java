package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ArtistRetriever {

    private final ArtistRepository artistRepository;
    private final ArtistCreator artistCreator;

    @Autowired
    public ArtistRetriever(ArtistRepository artistRepository, ArtistCreator artistCreator) {
        this.artistRepository = artistRepository;
        this.artistCreator = artistCreator;
    }

    public Artist getOrCreate(ArtistDto artistDto) {
        if (artistDto.getId() != null) {
            return artistRepository.findByIdOrThrow(artistDto.getId());
        }

        return artistCreator.createManually(artistDto);
    }

    public Artist getOrCreateBySpotify(ArtistSpotifyDto artistSpotifyDto) {
        Artist artist = artistRepository.findBySpotifyId(artistSpotifyDto.id());

        if (artist != null) {
            return artist;
        }

        return artistCreator.createFromSpotify(artistSpotifyDto);
    }
}
