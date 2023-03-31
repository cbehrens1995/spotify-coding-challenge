package org.cbehrens.spotifycodingchallenge.commons;

public abstract class AbstractSpotifyDtoBuilder<S extends AbstractSpotifyDto, B extends AbstractSpotifyDtoBuilder<S, B>> extends AbstractDtoBuilder<S, B> {

    protected String externalSpotifyUrl;
    protected String spotifyId;
    protected String uri;
    protected Origin origin;
    protected boolean manuallyAdjusted;

    protected AbstractSpotifyDtoBuilder(Long id) {
        super(id);
    }

    public B withExternalSpotifyUrl(String externalSpotifyUrl) {
        this.externalSpotifyUrl = externalSpotifyUrl;
        return (B) this;
    }

    public B withSpotifyId(String spotifyId) {
        this.spotifyId = spotifyId;
        return (B) this;
    }

    public B withUri(String uri) {
        this.uri = uri;
        return (B) this;
    }

    public B withOrigin(Origin origin) {
        this.origin = origin;
        return (B) this;
    }

    public B withManuallyAdjusted(boolean manuallyAdjusted) {
        this.manuallyAdjusted = manuallyAdjusted;
        return (B) this;
    }


}