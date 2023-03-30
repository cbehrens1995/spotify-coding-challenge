package org.cbehrens.spotifycodingchallenge.commons;

import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AbstractRepository<E extends AbstractEntity> extends PagingAndSortingRepository<E, Long> {

    default E findByIdOrThrow(long id) {
        Optional<E> optionalEntity = findById(id);
        return optionalEntity
                .orElseThrow(() -> new ResourceNotFoundException("Id does not exist!"));
    }
}
