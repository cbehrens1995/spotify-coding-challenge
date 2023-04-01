package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyDtoBuilder;

public class ArtistDtoBuilder extends AbstractSpotifyDtoBuilder<ArtistDto, ArtistDtoBuilder> {

    private Integer followersCount;
    private String imageUrl;
    private String name;
    private Integer popularity;

    private ArtistDtoBuilder(Long id) {
        super(id);
    }

    public static ArtistDtoBuilder artistDto(Long id) {
        return new ArtistDtoBuilder(id);
    }

    public ArtistDtoBuilder withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    public ArtistDtoBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ArtistDtoBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ArtistDtoBuilder withPopularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    @Override
    public ArtistDto build() {
        return new ArtistDto(id, externalSpotifyUrl, spotifyId, uri, origin, manuallyAdjusted, followersCount, imageUrl, name, popularity);
    }
}