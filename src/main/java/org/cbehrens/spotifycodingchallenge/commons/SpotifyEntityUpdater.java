package org.cbehrens.spotifycodingchallenge.commons;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class SpotifyEntityUpdater<S extends AbstractSpotifyEntity> {

    protected  <T> boolean updateIfChanged(S spotifyEntity, Function<S, T> getterFunction, BiConsumer<S, T> setter, T newValue) {
        T oldValue = getterFunction.apply(spotifyEntity);
        if (!Objects.equals(oldValue, newValue)) {
            setter.accept(spotifyEntity, newValue);
            return true;
        }
        return false;
    }
}
