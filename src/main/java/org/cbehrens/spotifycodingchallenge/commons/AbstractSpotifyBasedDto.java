package org.cbehrens.spotifycodingchallenge.commons;

public abstract class AbstractSpotifyBasedDto extends AbstractDto {

    private String externalSpotifyUrl;
    private String spotifyId;
    private String uri;
    private Origin origin;
    private Boolean manuallyAdjusted;

    protected AbstractSpotifyBasedDto(Long id, String externalSpotifyUrl, String spotifyId, String uri, Origin origin, Boolean manuallyAdjusted) {
        super(id);
        this.externalSpotifyUrl = externalSpotifyUrl;
        this.spotifyId = spotifyId;
        this.uri = uri;
        this.origin = origin;
        this.manuallyAdjusted = manuallyAdjusted;
    }

    public String getExternalSpotifyUrl() {
        return externalSpotifyUrl;
    }

    public String getSpotifyId() {
        return spotifyId;
    }

    public String getUri() {
        return uri;
    }

    public Origin getOrigin() {
        return origin;
    }

    public Boolean getManuallyAdjusted() {
        return manuallyAdjusted;
    }
}
