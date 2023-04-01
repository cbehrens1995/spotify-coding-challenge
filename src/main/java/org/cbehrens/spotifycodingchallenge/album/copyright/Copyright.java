package org.cbehrens.spotifycodingchallenge.album.copyright;

import org.cbehrens.spotifycodingchallenge.album.Album;
import org.cbehrens.spotifycodingchallenge.commons.AbstractEntity;

import javax.persistence.*;

@Entity
@Table(name = "copyright")
public class Copyright extends AbstractEntity {

    @Column(name = "copyright_text")
    private String text;

    @Column(name = "copyright_type")
    @Enumerated(EnumType.STRING)
    private CopyrightType copyrightType;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_album")
    private Album album;

    protected Copyright() {
    }

    public Copyright(String text, CopyrightType copyrightType, Album album) {
        this.text = text;
        this.copyrightType = copyrightType;
        this.album = album;
    }

    public String getText() {
        return text;
    }

    public CopyrightType getCopyrightType() {
        return copyrightType;
    }

    public Album getAlbum() {
        return album;
    }
}
