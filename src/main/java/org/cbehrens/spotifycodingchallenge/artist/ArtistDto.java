package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.Origin;

public class ArtistDto extends AbstractSpotifyDto {

    private Integer followersCount;
    private String imageUrl;
    private String name;
    private Integer popularity;

    public ArtistDto(Long id, String externalSpotifyUrl, String spotifyId, String uri, Origin origin, Boolean manuallyAdjusted, Integer followersCount, String imageUrl, String name, Integer popularity) {
        super(id, externalSpotifyUrl, spotifyId, uri, origin, manuallyAdjusted);
        this.followersCount = followersCount;
        this.imageUrl = imageUrl;
        this.name = name;
        this.popularity = popularity;
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getName() {
        return name;
    }

    public Integer getPopularity() {
        return popularity;
    }
}
