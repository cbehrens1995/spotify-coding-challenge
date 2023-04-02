package org.cbehrens.spotifycodingchallenge.album.copyright;

import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;

public class CopyrightDto extends AbstractDto {

    private String text;
    private CopyrightType copyrightType;

    public CopyrightDto(Long id, String text, CopyrightType copyrightType) {
        super(id);
        this.text = text;
        this.copyrightType = copyrightType;
    }

    public String getText() {
        return text;
    }

    public CopyrightType getCopyrightType() {
        return copyrightType;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCopyrightType(CopyrightType copyrightType) {
        this.copyrightType = copyrightType;
    }
}
