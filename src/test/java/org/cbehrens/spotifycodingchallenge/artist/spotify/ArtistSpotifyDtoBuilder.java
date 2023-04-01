package org.cbehrens.spotifycodingchallenge.artist.spotify;

import java.util.ArrayList;
import java.util.List;

public class ArtistSpotifyDtoBuilder {

    private String externalSpotifyUrl;
    private Integer followersCount;
    private String id;
    private String name;
    private Integer popularity;
    private String uri;
    private List<String> imageUrls = new ArrayList<>();

    private ArtistSpotifyDtoBuilder() {
    }

    public static ArtistSpotifyDtoBuilder artistSpotifyDto() {
        return new ArtistSpotifyDtoBuilder();
    }

    public ArtistSpotifyDtoBuilder withExternalUrls(String externalUrls) {
        this.externalSpotifyUrl = externalUrls;
        return this;
    }

    public ArtistSpotifyDtoBuilder withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    public ArtistSpotifyDtoBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public ArtistSpotifyDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ArtistSpotifyDtoBuilder withPopularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public ArtistSpotifyDtoBuilder withUri(String uri) {
        this.uri = uri;
        return this;
    }

    public ArtistSpotifyDtoBuilder addImageUrl(String imageUrl) {
        imageUrls.add(imageUrl);
        return this;
    }

    public ArtistSpotifyDto build() {
        var externalUrls = new ExternalUrls(externalSpotifyUrl);
        var followers = new Followers(null, followersCount);
        var images = imageUrls.stream()
                .map(imageUrl -> new Image(imageUrl, null, null))
                .toList();
        return new ArtistSpotifyDto(externalUrls, followers, id, images, name, popularity, uri);
    }

}