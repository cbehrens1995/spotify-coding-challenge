package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.artist.ArtistDto;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyBasedDtoBuilder;

import java.util.ArrayList;
import java.util.List;

public class AlbumDtoBuilder extends AbstractSpotifyBasedDtoBuilder<AlbumDto, AlbumDtoBuilder> {

    private Integer trackCount;
    private String imageUrl;
    private String name;
    private String releaseDate;
    private ReleaseDatePrecision releaseDatePrecision;
    private RestrictionReason restrictionReason;
    private AlbumType albumType;
    private List<CopyrightDto> copyrights = new ArrayList<>();
    private List<ArtistDto> artistDtos = new ArrayList<>();

    private AlbumDtoBuilder(Long id) {
        super(id);
    }

    public static AlbumDtoBuilder albumDto(Long id) {
        return new AlbumDtoBuilder(id);
    }

    public AlbumDtoBuilder withTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
        return this;
    }

    public AlbumDtoBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public AlbumDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AlbumDtoBuilder withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public AlbumDtoBuilder withReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
        return this;
    }

    public AlbumDtoBuilder withRestrictionReason(RestrictionReason restrictionReason) {
        this.restrictionReason = restrictionReason;
        return this;
    }

    public AlbumDtoBuilder withAlbumType(AlbumType albumType) {
        this.albumType = albumType;
        return this;
    }

    public AlbumDtoBuilder addCopyrightDto(CopyrightDto copyright) {
        copyrights.add(copyright);
        return this;
    }

    public AlbumDtoBuilder addArtistDto(ArtistDto artistDto) {
        artistDtos.add(artistDto);
        return this;
    }

    @Override
    protected AlbumDto build() {
        return new AlbumDto(id, externalSpotifyUrl, spotifyId, uri, origin, manuallyAdjusted, trackCount, imageUrl, name,
                releaseDate, releaseDatePrecision, restrictionReason, albumType, copyrights, artistDtos);
    }
}