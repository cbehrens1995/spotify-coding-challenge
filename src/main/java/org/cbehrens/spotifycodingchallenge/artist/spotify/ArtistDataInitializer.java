package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.cbehrens.spotifycodingchallenge.album.*;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumByArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumSpotifyDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumsByArtistSpotifyDto;
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
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ArtistDataInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArtistDataInitializer.class);

    @Value(value = "${artist.spotify.ids}")
    private List<String> artistSpotifyIds;
    @Value(value = "${artist.spotify.albumGroups}")
    private List<String> includedGroups;
    @Value(value = "${artist.spotify.market}")
    private String market;
    @Value(value = "${artist.spotify.offset}")
    private Integer offset;
    @Value(value = "${artist.spotify.limit}")
    private Integer limit;

    private final ArtistSpotifyClient artistSpotifyClient;
    private final ArtistCreator artistCreator;
    private final ArtistRepository artistRepository;
    private final AlbumRepository albumRepository;
    private final AlbumCreator albumCreator;
    private final AlbumSpotifyClient albumSpotifyClient;
    private final AlbumUpdater albumUpdater;

    @Autowired
    public ArtistDataInitializer(ArtistSpotifyClient artistSpotifyClient, ArtistCreator artistCreator, ArtistRepository artistRepository, AlbumRepository albumRepository, AlbumCreator albumCreator, AlbumSpotifyClient albumSpotifyClient, AlbumUpdater albumUpdater) {
        this.artistSpotifyClient = artistSpotifyClient;
        this.artistCreator = artistCreator;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.albumCreator = albumCreator;
        this.albumSpotifyClient = albumSpotifyClient;
        this.albumUpdater = albumUpdater;
    }

    @PostConstruct
    public void initArtists() {
        ArtistSpotifyWrapperDto artistSpotifyWrapperDto = artistSpotifyClient.getArtists(artistSpotifyIds);
        List<Artist> artists = artistSpotifyWrapperDto.artistSpotifyDtos().stream()
                .filter(artistSpotifyDto -> !artistRepository.existsBySpotifyId(artistSpotifyDto.id()))
                .map(artistCreator::createFromSpotify)
                .toList();
        LOGGER.info("Initialized {} artists", artists.size());

        artists.forEach(this::initAlbumsForArtist);
    }

    private void initAlbumsForArtist(Artist artist) {
        AlbumsByArtistSpotifyDto albumsByArtistSpotifyDto = artistSpotifyClient.getAlbumsByArtist(artist.getSpotifyId(), includedGroups, market, limit, offset);
        List<AlbumByArtistSpotifyDto> albumByArtistSpotifyDtos = albumsByArtistSpotifyDto.albumByArtistSpotifyDtos();

        createNewAlbumsForArtist(artist, albumByArtistSpotifyDtos);

        Set<String> albumSpotifyIdsWithNewArtist = albumByArtistSpotifyDtos.stream()
                .map(AlbumByArtistSpotifyDto::id)
                .filter(id -> albumRepository.existsBySpotifyIdAndNotArtist(id, artist))
                .collect(Collectors.toSet());
        if (!albumSpotifyIdsWithNewArtist.isEmpty()) {
            LOGGER.warn("Found {} albums where the artists have changed!", albumSpotifyIdsWithNewArtist.size());
            albumSpotifyIdsWithNewArtist.forEach(this::updateExistingAlbum);
        }
    }

    /**
     * When retrieving the albums for an artist, those albums could contain several other artists as well for instance
     * for a compilation. Furthermore, the dto does not give full information about the artist like when requesting
     * the artist endpoint. In consequence, we have to retrieve all artist data manually for a given album afterwards.
     * To ensure integrity we switch from artist based to album based using a grouped map.
     * The visibility has been should for testing purposes
     */
    void createNewAlbumsForArtist(Artist artist, List<AlbumByArtistSpotifyDto> albumByArtistSpotifyDtos) {
        Map<AlbumByArtistSpotifyDto, List<String>> artistSpotifyIdsByAlbumSpotifyId = albumByArtistSpotifyDtos.stream()
                .filter(albumByArtistSpotifyDto -> !albumRepository.existsBySpotifyId(albumByArtistSpotifyDto.id()))
                .collect(Collectors.toMap(Function.identity(),
                        albumByArtistSpotifyDto -> albumByArtistSpotifyDto.artistShortSpotifyDtos().stream()
                                .map(ArtistShortSpotifyDto::id)
                                .toList()));

        for (Map.Entry<AlbumByArtistSpotifyDto, List<String>> artistSpotifyIdsByAlbumSpotifyIdEntry : artistSpotifyIdsByAlbumSpotifyId.entrySet()) {
            AlbumByArtistSpotifyDto albumByArtistSpotifyDto = artistSpotifyIdsByAlbumSpotifyIdEntry.getKey();
            List<String> artistSpotifyIdsForAlbum = artistSpotifyIdsByAlbumSpotifyIdEntry.getValue();
            ArtistSpotifyWrapperDto artistSpotifyWrapperDto = artistSpotifyClient.getArtists(artistSpotifyIdsForAlbum);
            List<ArtistSpotifyDto> artistSpotifyDtos = artistSpotifyWrapperDto.artistSpotifyDtos();
            albumCreator.create(albumByArtistSpotifyDto, artistSpotifyDtos);
        }

        LOGGER.info("Initialized {} albums for artist {}",
                artistSpotifyIdsByAlbumSpotifyId.keySet().size(), artist.getId());
    }

    private void updateExistingAlbum(String spotifyId) {
        AlbumSpotifyDto albumSpotifyDto = albumSpotifyClient.getAlbum(spotifyId);
        Album album = albumRepository.findBySpotifyId(spotifyId);
        albumUpdater.update(album, albumSpotifyDto);
        LOGGER.info("Updated album {}", album.getId());
    }
}
