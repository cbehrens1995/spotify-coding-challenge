package org.cbehrens.spotifycodingchallenge.album.copyright;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CopyrightConvertService {

    public CopyrightDto toDto(Copyright copyright) {
        return new CopyrightDto(copyright.getId(), copyright.getCopyrightText(), copyright.getCopyrightType());
    }

    public List<CopyrightDto> toDtos(List<Copyright> copyrights){
        return copyrights.stream()
                .map(this::toDto)
                .toList();
    }
}
