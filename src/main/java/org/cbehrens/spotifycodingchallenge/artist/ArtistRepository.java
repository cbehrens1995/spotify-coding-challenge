package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistRepository extends AbstractRepository<Artist> {

    boolean existsBySpotifyId(String spotifyId);

    Artist findBySpotifyId(String spotifyId);

    @Query("""
            SELECT artist
            FROM Artist artist
            WHERE artist.manuallyAdjusted IS FALSE 
            AND artist.origin = org.cbehrens.spotifycodingchallenge.commons.Origin.SPOTIFY
            """)
    List<Artist> findUnmodifiedSpotifyArtists();
}
