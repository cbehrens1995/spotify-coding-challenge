package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyBasedEntity;
import org.cbehrens.spotifycodingchallenge.commons.Origin;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "album")
public class Album extends AbstractSpotifyBasedEntity {

    @Column(name = "track_count")
    private Integer trackCount;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "album_name")
    private String albumNname;

    @Column(name = "release_date")
    private String releaseDate;

    @Column(name = "release_date_precision")
    @Enumerated(EnumType.STRING)
    private ReleaseDatePrecision releaseDatePrecision;

    @Column(name = "restriction_reason")
    @Enumerated(EnumType.STRING)
    private RestrictionReason restrictionReason;

    @Column(name = "album_type")
    @Enumerated(EnumType.STRING)
    private AlbumType albumType;

    @OneToMany(mappedBy = "album", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Copyright> copyrights = new ArrayList<>();

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "album_to_artist",
            joinColumns = @JoinColumn(name = "fk_album"),
            inverseJoinColumns = @JoinColumn(name = "fk_artist")
    )
    private List<Artist> artists = new ArrayList<>();

    protected Album() {
    }

    public Album(String externalSpotifyUrl, String spotifyId, String uri, Origin origin, Integer trackCount, String imageUrl, String albumNname, String releaseDate, ReleaseDatePrecision releaseDatePrecision, RestrictionReason restrictionReason, AlbumType albumType) {
        super(externalSpotifyUrl, spotifyId, uri, origin);
        this.trackCount = trackCount;
        this.imageUrl = imageUrl;
        this.albumNname = albumNname;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.restrictionReason = restrictionReason;
        this.albumType = albumType;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public void setTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAlbumNname() {
        return albumNname;
    }

    public void setAlbumNname(String name) {
        this.albumNname = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public ReleaseDatePrecision getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public void setReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
    }

    public RestrictionReason getRestrictionReason() {
        return restrictionReason;
    }

    public void setRestrictionReason(RestrictionReason restrictionReason) {
        this.restrictionReason = restrictionReason;
    }

    public AlbumType getAlbumType() {
        return albumType;
    }

    public void setAlbumType(AlbumType albumType) {
        this.albumType = albumType;
    }

    public List<Copyright> getCopyrights() {
        return copyrights;
    }

    public void addCopyright(Copyright copyright) {
        copyrights.add(copyright);
    }

    public void clearCopyrights() {
        copyrights.clear();
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void addArtist(Artist artist) {
        artists.add(artist);
    }

    public void clearArtist() {
        artists.clear();
    }
}
