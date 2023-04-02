package org.cbehrens.spotifycodingchallenge.album.spotify;

import org.cbehrens.spotifycodingchallenge.album.AlbumType;
import org.cbehrens.spotifycodingchallenge.album.ReleaseDatePrecision;
import org.cbehrens.spotifycodingchallenge.album.RestrictionReason;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistShortSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.spotify.ExternalUrls;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;

import java.util.ArrayList;
import java.util.List;

public class AlbumByArtistSpotifyDtoBuilder {

    private AlbumType albumType;
    private Integer trackCount;
    private String externalUrlsString;
    private String id;
    private String name;
    private String releaseDate;
    private ReleaseDatePrecision releaseDatePrecision;
    private RestrictionReason restrictionReason;
    private String uri;
    private List<String> imageUrls = new ArrayList<>();
    private List<CopyrightSpotifyDto> copyrightSpotifyDtos = new ArrayList<>();
    private List<ArtistShortSpotifyDto> artistShortSpotifyDtos = new ArrayList<>();

    private AlbumByArtistSpotifyDtoBuilder() {
    }

    public static AlbumByArtistSpotifyDtoBuilder albumByArtistSpotifyDto() {
        return new AlbumByArtistSpotifyDtoBuilder();
    }

    public AlbumByArtistSpotifyDtoBuilder withAlbumType(AlbumType albumType) {
        this.albumType = albumType;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withTrackCount(Integer trackCount) {
        this.trackCount = trackCount;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withExternalSpotifyUrl(String externalUrls) {
        this.externalUrlsString = externalUrls;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withReleaseDatePrecision(ReleaseDatePrecision releaseDatePrecision) {
        this.releaseDatePrecision = releaseDatePrecision;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withRestrictionReason(RestrictionReason restrictionReason) {
        this.restrictionReason = restrictionReason;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder addCopyrightSpotifyDto(CopyrightSpotifyDto copyrightSpotifyDto) {
        copyrightSpotifyDtos.add(copyrightSpotifyDto);
        return this;
    }

    public AlbumByArtistSpotifyDtoBuilder addArtistShortSpotifyDto(ArtistShortSpotifyDto artistShortSpotifyDto) {
        artistShortSpotifyDtos.add(artistShortSpotifyDto);
        return this;
    }

    public AlbumByArtistSpotifyDto build() {
        var externalUrls = new ExternalUrls(externalUrlsString);
        var images = imageUrls.stream()
                .map(imageUrl -> new Image(imageUrl, null, null))
                .toList();
        var restriction = new Restriction(restrictionReason);
        return new AlbumByArtistSpotifyDto(albumType, trackCount, externalUrls, id, images, name, releaseDate, releaseDatePrecision,
                restriction, uri, copyrightSpotifyDtos, artistShortSpotifyDtos);
    }

}