package org.cbehrens.spotifycodingchallenge.commons;

import org.apache.commons.lang3.BooleanUtils;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractSpotifyEntity extends AbstractEntity {

    @Column(name = "external_spotify_url")
    private String externalSpotifyUrl;

    @Column(name = "spotify_id")
    private String spotifyId;

    @Column(name = "uri")
    private String uri;

    @Column(name = "origin")
    @Enumerated(EnumType.STRING)
    private Origin origin;

    @Column(name = "manually_adjusted")
    private Boolean manuallyAdjusted;

    public AbstractSpotifyEntity(String externalSpotifyUrl, String spotifyId, String uri, Origin origin) {
        this.externalSpotifyUrl = externalSpotifyUrl;
        this.spotifyId = spotifyId;
        this.uri = uri;
        this.origin = origin;
        this.manuallyAdjusted = false;
    }

    public AbstractSpotifyEntity() {
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

    public boolean isManuallyAdjusted() {
        return BooleanUtils.isTrue(manuallyAdjusted);
    }

    public void setManuallyAdjusted() {
        this.manuallyAdjusted = true;
    }
}
