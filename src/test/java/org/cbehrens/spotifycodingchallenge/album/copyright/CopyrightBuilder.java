package org.cbehrens.spotifycodingchallenge.album.copyright;

import org.cbehrens.spotifycodingchallenge.album.Album;
import org.cbehrens.spotifycodingchallenge.commons.AbstractEntityBuilder;

public class CopyrightBuilder extends AbstractEntityBuilder<Copyright, CopyrightBuilder> {

    private String text;
    private CopyrightType copyrightType;
    private Album album;

    private CopyrightBuilder(Long id) {
        super(id);
    }

    public static CopyrightBuilder copyright(Long id) {
        return new CopyrightBuilder(id);
    }

    public CopyrightBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CopyrightBuilder withCopyrightType(CopyrightType copyrightType) {
        this.copyrightType = copyrightType;
        return this;
    }

    public CopyrightBuilder withAlbum(Album album) {
        this.album = album;
        return this;
    }

    public Copyright build() {
        var copyright = new Copyright(text, copyrightType, album);
        return super.build(copyright);
    }
}