package org.cbehrens.spotifycodingchallenge.commons;

import org.springframework.test.util.ReflectionTestUtils;

public abstract class AbstractEntityBuilder<E extends AbstractEntity, B extends AbstractEntityBuilder<E, B>> {

    private Long id;

    protected AbstractEntityBuilder(Long id) {
        this.id = id;
    }

    protected E build(E entity) {
        ReflectionTestUtils.setField(entity, "id", id);
        return entity;
    }
}