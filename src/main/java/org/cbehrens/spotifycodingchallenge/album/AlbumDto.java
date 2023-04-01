package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.artist.ArtistDto;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.Origin;

import java.util.List;

public class AlbumDto extends AbstractSpotifyDto {

    private Integer trackCount;
    private String imageUrl;
    private String name;
    private String releaseDate;
    private ReleaseDatePrecision releaseDatePrecision;
    private RestrictionReason restrictionReason;
    private AlbumType albumType;
    private List<CopyrightDto> copyrightDtos;
    private List<ArtistDto> artistDtos;

    public AlbumDto(Long id, String externalSpotifyUrl, String spotifyId, String uri, Origin origin, Boolean manuallyAdjusted, Integer trackCount, String imageUrl, String name, String releaseDate, ReleaseDatePrecision releaseDatePrecision, RestrictionReason restrictionReason, AlbumType albumType, List<CopyrightDto> copyrightDtos, List<ArtistDto> artistDtos) {
        super(id, externalSpotifyUrl, spotifyId, uri, origin, manuallyAdjusted);
        this.trackCount = trackCount;
        this.imageUrl = imageUrl;
        this.name = name;
        this.releaseDate = releaseDate;
        this.releaseDatePrecision = releaseDatePrecision;
        this.restrictionReason = restrictionReason;
        this.albumType = albumType;
        this.copyrightDtos = copyrightDtos;
        this.artistDtos = artistDtos;
    }

    public Integer getTrackCount() {
        return trackCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ReleaseDatePrecision getReleaseDatePrecision() {
        return releaseDatePrecision;
    }

    public RestrictionReason getRestrictionReason() {
        return restrictionReason;
    }

    public AlbumType getAlbumType() {
        return albumType;
    }

    public List<CopyrightDto> getCopyrightDtos() {
        return copyrightDtos;
    }

    public List<ArtistDto> getArtistDtos() {
        return artistDtos;
    }
}
