package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyBasedEntityBuilder;

import java.util.ArrayList;
import java.util.List;

public class AlbumBuilder extends AbstractSpotifyBasedEntityBuilder<Album, AlbumBuilder> {

    private Integer trackCount;
    private String imageUrl;
    private String name;
    private String releaseDate;
    private ReleaseDatePrecision releaseDatePrecision;
    private RestrictionReason restrictionReason;
    private AlbumType albumType;

    private List<Copyright> copyrights = new ArrayList<>();
    private List<Artist> artists = new ArrayList<>();

    private AlbumBuilder(Long id) {
        super(id);
    }

    public static AlbumBuilder album(Long id) {
        return new AlbumBuilder(id);
    }

    public AlbumBuilder withTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
        return this;
    }

    public AlbumBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public AlbumBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AlbumBuilder withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public AlbumBuilder withReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
        return this;
    }

    public AlbumBuilder withRestrictionReason(RestrictionReason restrictionReason) {
        this.restrictionReason = restrictionReason;
        return this;
    }

    public AlbumBuilder withAlbumType(AlbumType albumType) {
        this.albumType = albumType;
        return this;
    }


    public AlbumBuilder addCopyright(Copyright copyright) {
        copyrights.add(copyright);
        return this;
    }

    public AlbumBuilder addArtist(Artist artist) {
        artists.add(artist);
        return this;
    }

    public Album build() {
        var album = new Album(externalSpotifyUrl, spotifyId, uri, origin, trackCount, imageUrl, name, releaseDate, releaseDatePrecision, restrictionReason, albumType);
        copyrights.forEach(album::addCopyright);
        artists.forEach(album::addArtist);
        return super.build(album);
    }

}