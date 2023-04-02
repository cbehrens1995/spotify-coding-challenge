package org.cbehrens.spotifycodingchallenge.album.spotify;

import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightType;

public class CopyrightSpotifyDtoBuilder {

    private String text;
    private CopyrightType type;

    private CopyrightSpotifyDtoBuilder() {
    }

    public static CopyrightSpotifyDtoBuilder copyrightSpotifyDto() {
        return new CopyrightSpotifyDtoBuilder();
    }

    public CopyrightSpotifyDtoBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CopyrightSpotifyDtoBuilder withCopyrightType(CopyrightType type) {
        this.type = type;
        return this;
    }

    public CopyrightSpotifyDto build() {
        return new CopyrightSpotifyDto(text, type);
    }
}