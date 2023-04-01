package org.cbehrens.spotifycodingchallenge.album.copyright;

import org.cbehrens.spotifycodingchallenge.commons.AbstractDtoBuilder;

public class CopyrightDtoBuilder extends AbstractDtoBuilder<CopyrightDto, CopyrightDtoBuilder> {

    private String text;
    private CopyrightType copyrightType;

    private CopyrightDtoBuilder(Long id) {
        super(id);
    }

    public static CopyrightDtoBuilder copyrightDto(Long id) {
        return new CopyrightDtoBuilder(id);
    }

    public CopyrightDtoBuilder withText(String text) {
        this.text = text;
        return this;
    }

    public CopyrightDtoBuilder withCopyrightType(CopyrightType copyrightType) {
        this.copyrightType = copyrightType;
        return this;
    }

    @Override
    public CopyrightDto build() {
        return new CopyrightDto(id, text, copyrightType);
    }
}