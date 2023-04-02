package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.commons.AbstractRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AlbumRepository extends AbstractRepository<Album> {

    boolean existsBySpotifyId(String spotifyId);

    Album findBySpotifyId(String spotifyId);

    @Query("""
            SELECT COUNT(album.id)>0
            FROM Album album
            WHERE album.spotifyId = :spotifyId
            AND album.manuallyAdjusted IS FALSE
            AND NOT EXISTS (
                SELECT 1
                FROM Album album
                JOIN album.artists artist
                WHERE album.spotifyId = :spotifyId
                AND album.manuallyAdjusted IS FALSE
                AND artist = :artist)
            """)
    boolean existsBySpotifyIdAndNotArtist(@Param("spotifyId") String spotifyId,
                                          @Param("artist") Artist artist);
}
