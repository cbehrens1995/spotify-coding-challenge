package org.cbehrens.spotifycodingchallenge.commons;

public class AbstractDto {

    private Long id;

    public AbstractDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
