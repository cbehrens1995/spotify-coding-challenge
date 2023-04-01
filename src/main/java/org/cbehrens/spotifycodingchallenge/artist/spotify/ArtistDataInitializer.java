package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.artist.ArtistCreator;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRepository;
import org.cbehrens.spotifycodingchallenge.artist.ArtistSpotifyClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class ArtistDataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistDataInitializer.class);

    @Value(value = "${artist.spotify.ids}")
    private List<String> artistSpotifyIds;

    private final ArtistSpotifyClient artistSpotifyClient;
    private final ArtistCreator artistCreator;
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistDataInitializer(ArtistSpotifyClient artistSpotifyClient, ArtistCreator artistCreator, ArtistRepository artistRepository) {
        this.artistSpotifyClient = artistSpotifyClient;
        this.artistCreator = artistCreator;
        this.artistRepository = artistRepository;
    }

    @PostConstruct
    public void initArtists() {
        ArtistSpotifyWrapperDto artistSpotifyWrapperDto = artistSpotifyClient.getArtists(artistSpotifyIds);
        List<Artist> artists = artistSpotifyWrapperDto.artistSpotifyDtos().stream()
                .filter(artistSpotifyDto -> !artistRepository.existsBySpotifyId(artistSpotifyDto.id()))
                .map(artistCreator::createFromSpotify)
                .toList();
        LOGGER.info("Initialized {} artistSpotifyDtos on start up", artists.size());
    }
}
