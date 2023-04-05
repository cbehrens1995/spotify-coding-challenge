package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.apache.commons.collections4.ListUtils;
import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRepository;
import org.cbehrens.spotifycodingchallenge.artist.ArtistSpotifyClient;
import org.cbehrens.spotifycodingchallenge.artist.ArtistUpdater;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyBasedEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ScheduledArtistUpdater {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledArtistUpdater.class);

    @Value(value = "${artist.spotify.scheduled.partion.size}")
    private int partitionSize;

    private final ArtistRepository artistRepository;
    private final ArtistSpotifyClient artistSpotifyClient;
    private final ArtistUpdater artistUpdater;

    @Autowired
    public ScheduledArtistUpdater(ArtistRepository artistRepository, ArtistSpotifyClient artistSpotifyClient, ArtistUpdater artistUpdater) {
        this.artistRepository = artistRepository;
        this.artistSpotifyClient = artistSpotifyClient;
        this.artistUpdater = artistUpdater;
    }

    @Scheduled(cron = "0 0 * * * *", zone = "Europe/Paris")
    public void updateArtists() {
        List<Artist> unmodifiedSpotifyArtists = artistRepository.findUnmodifiedSpotifyArtists();
        LOGGER.info("Found {} unmodified artists from spotify scheduled for update", unmodifiedSpotifyArtists.size());

        List<List<Artist>> artistPartitions = ListUtils.partition(unmodifiedSpotifyArtists, partitionSize);
        artistPartitions.forEach(this::updateArtistPartitioned);
        LOGGER.info("Finished scheduled update of artists");
    }

    private void updateArtistPartitioned(List<Artist> unmodifiedSpotifyArtists) {
        List<String> artistSpotifyIds = unmodifiedSpotifyArtists.stream()
                .map(AbstractSpotifyBasedEntity::getSpotifyId)
                .toList();
        ArtistSpotifyWrapperDto artistSpotifyWrapperDto = artistSpotifyClient.getArtists(artistSpotifyIds);
        List<ArtistSpotifyDto> artistSpotifyDtos = artistSpotifyWrapperDto.artistSpotifyDtos();

        Map<Artist, ArtistSpotifyDto> artistSpotifyDtoByArtist = new HashMap<>();
        for (Artist unmodifiedSpotifyArtist : unmodifiedSpotifyArtists) {
            List<ArtistSpotifyDto> filteredArtistSpotifyDtos = artistSpotifyDtos.stream()
                    .filter(artistSpotifyDto -> artistSpotifyDto.id().equals(unmodifiedSpotifyArtist.getSpotifyId()))
                    .toList();
            Assert.isTrue(filteredArtistSpotifyDtos.size() == 1,
                    String.format("Received non unique response artist response for artist %s", unmodifiedSpotifyArtist.getId()));

            artistSpotifyDtoByArtist.put(unmodifiedSpotifyArtist, filteredArtistSpotifyDtos.get(0));
        }

        artistSpotifyDtoByArtist.forEach(artistUpdater::updateFromSpotify);
    }
}
