package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.cbehrens.spotifycodingchallenge.commons.spotify.ExternalUrls;

public class ArtistShortSpotifyDtoBuilder {

    private String externalSpotifyUrls;
    private String id;
    private String name;
    private String uri;

    private ArtistShortSpotifyDtoBuilder() {
    }

    public static ArtistShortSpotifyDtoBuilder artistShortSpotifyDto() {
        return new ArtistShortSpotifyDtoBuilder();
    }

    public ArtistShortSpotifyDtoBuilder withExternalSpotifyUrls(String externalSpotifyUrls) {
        this.externalSpotifyUrls = externalSpotifyUrls;
        return this;
    }

    public ArtistShortSpotifyDtoBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ArtistShortSpotifyDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ArtistShortSpotifyDtoBuilder withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public ArtistShortSpotifyDto build() {
        var externalUrls = new ExternalUrls(externalSpotifyUrls);
        return new ArtistShortSpotifyDto(externalUrls, id, name, uri);
    }

}