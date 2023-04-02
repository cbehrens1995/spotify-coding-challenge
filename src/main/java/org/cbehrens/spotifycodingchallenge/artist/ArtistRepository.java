package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractRepository;

public interface ArtistRepository extends AbstractRepository<Artist> {

    boolean existsBySpotifyId(String spotifyId);

    Artist findBySpotifyId(String spotifyId);
}
