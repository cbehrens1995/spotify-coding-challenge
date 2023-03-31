package org.cbehrens.spotifycodingchallenge.commons;

public abstract class AbstractDtoBuilder<D extends AbstractDto, B extends AbstractDtoBuilder<D, B>> {

    protected Long id;

    protected AbstractDtoBuilder(Long id) {
        this.id = id;
    }

    protected abstract D build();

}