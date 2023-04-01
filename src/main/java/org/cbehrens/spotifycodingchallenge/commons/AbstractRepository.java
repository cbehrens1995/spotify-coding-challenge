package org.cbehrens.spotifycodingchallenge.commons;

import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface AbstractRepository<E extends AbstractEntity> extends PagingAndSortingRepository<E, Long> {

    default E findByIdOrThrow(long id) {
        Optional<E> optionalEntity = findById(id);
        return optionalEntity
                .orElseThrow(() -> new ResourceNotFoundException("Id does not exist!"));
    }

    default List<E> findAllByIdOrThrow(List<Long> ids) {
        List<E> entities = IterableUtils.toList(findAllById(ids));
        if (entities.size() != ids.size()) {
            throw new ResourceNotFoundException("Not all Ids exist!");
        }

        return entities;
    }
}
