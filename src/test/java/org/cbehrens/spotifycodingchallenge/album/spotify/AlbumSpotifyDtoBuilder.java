package org.cbehrens.spotifycodingchallenge.album.spotify;

import org.cbehrens.spotifycodingchallenge.album.AlbumType;
import org.cbehrens.spotifycodingchallenge.album.ReleaseDatePrecision;
import org.cbehrens.spotifycodingchallenge.album.RestrictionReason;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.spotify.ExternalUrls;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;

import java.util.ArrayList;
import java.util.List;

public class AlbumSpotifyDtoBuilder {
    AlbumType albumType;
    Integer trackCount;
    String externalSpotifyUrls;
    String id;
    String name;
    String releaseDate;
    ReleaseDatePrecision releaseDatePrecision;
    RestrictionReason restrictionReason;
    String uri;
    Integer popularity;
    List<String> imageUrls = new ArrayList<>();
    List<CopyrightSpotifyDto> copyrightSpotifyDtos = new ArrayList<>();
    List<ArtistSpotifyDto> artistSpotifyDtos = new ArrayList<>();

    private AlbumSpotifyDtoBuilder() {
    }

    public static AlbumSpotifyDtoBuilder albumSpotifyDto() {
        return new AlbumSpotifyDtoBuilder();
    }

    public AlbumSpotifyDtoBuilder withAlbumType(AlbumType albumType) {
        this.albumType = albumType;
        return this;
    }

    public AlbumSpotifyDtoBuilder withTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
        return this;
    }

    public AlbumSpotifyDtoBuilder withExternalSpotifyUrls(String externalSpotifyUrls) {
        this.externalSpotifyUrls = externalSpotifyUrls;
        return this;
    }

    public AlbumSpotifyDtoBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public AlbumSpotifyDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AlbumSpotifyDtoBuilder withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public AlbumSpotifyDtoBuilder withReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
        return this;
    }

    public AlbumSpotifyDtoBuilder withRestrictionReason(RestrictionReason restrictionReason) {
        this.restrictionReason = restrictionReason;
        return this;
    }

    public AlbumSpotifyDtoBuilder withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public AlbumSpotifyDtoBuilder withPopularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public AlbumSpotifyDtoBuilder addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
        return this;
    }

    public AlbumSpotifyDtoBuilder addCopyrightSpotifyDto(CopyrightSpotifyDto copyrightSpotifyDto) {
        copyrightSpotifyDtos.add(copyrightSpotifyDto);
        return this;
    }

    public AlbumSpotifyDtoBuilder addArtistSpotifyDto(ArtistSpotifyDto artistSpotifyDto) {
        artistSpotifyDtos.add(artistSpotifyDto);
        return this;
    }

    public AlbumSpotifyDto build() {
        var externalUrls = new ExternalUrls(externalSpotifyUrls);
        var images = imageUrls.stream()
                .map(imageUrl -> new Image(imageUrl, null, null))
                .toList();
        var restriction = new Restriction(restrictionReason);
        return new AlbumSpotifyDto(albumType, trackCount, externalUrls, id, images, name, releaseDate, releaseDatePrecision, restriction,
                uri, copyrightSpotifyDtos, popularity, artistSpotifyDtos);
    }


}