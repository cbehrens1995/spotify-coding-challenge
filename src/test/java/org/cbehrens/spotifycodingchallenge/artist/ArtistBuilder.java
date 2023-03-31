package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.album.Album;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyEntityBuilder;

import java.util.ArrayList;
import java.util.List;

public class ArtistBuilder extends AbstractSpotifyEntityBuilder<Artist, ArtistBuilder> {

    private Integer followersCount;
    private String imageUrl;
    private String name;
    private Integer popularity;
    private List<Album> albums = new ArrayList<>();


    private ArtistBuilder(Long id) {
        super(id);
    }

    public static ArtistBuilder artist(Long id) {
        return new ArtistBuilder(id);
    }

    public ArtistBuilder withFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
        return this;
    }

    public ArtistBuilder withImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public ArtistBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ArtistBuilder withPopularity(Integer popularity) {
        this.popularity = popularity;
        return this;
    }

    public ArtistBuilder addAlbum(Album album) {
        albums.add(album);
        return this;
    }

    public Artist build() {
        var artist = new Artist(externalSpotifyUrl, followersCount, spotifyId, imageUrl, name, popularity, uri, origin);
        albums.forEach(artist::addAlbum);
        return super.build(artist);
    }

}