package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.album.Album;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyBasedEntity;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.FullTextField;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.Indexed;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "artist")
@Indexed
public class Artist extends AbstractSpotifyBasedEntity {

    public static class FieldInfo {
        public static final String ARTIST_NAME = "artist_name";
    }

    @Column(name = "followers_count")
    private Integer followersCount;

    @Column(name = "image_url")
    private String imageUrl;

    @FullTextField(name = FieldInfo.ARTIST_NAME)
    @Column(name = "artist_name")
    private String name;

    @Column(name = "popularity")
    private Integer popularity;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "album_to_artist",
            joinColumns = @JoinColumn(name = "fk_artist"),
            inverseJoinColumns = @JoinColumn(name = "fk_album")
    )
    private List<Album> albums = new ArrayList<>();

    public Artist(String externalSpotifyUrl, Integer followersCount, String spotifyId, String imageUrl, String name, Integer popularity, String uri, Origin origin) {
        super(externalSpotifyUrl, spotifyId, uri, origin);
        this.followersCount = followersCount;
        this.imageUrl = imageUrl;
        this.name = name;
        this.popularity = popularity;
    }

    public Artist() {
    }

    public Integer getFollowersCount() {
        return followersCount;
    }

    public void setFollowersCount(Integer followersCount) {
        this.followersCount = followersCount;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void addAlbum(Album album) {
        albums.add(album);
    }

    public void removeAlbums() {
        albums.clear();
    }
}
