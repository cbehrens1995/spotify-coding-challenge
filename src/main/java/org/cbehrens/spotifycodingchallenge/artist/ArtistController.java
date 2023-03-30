package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController(value = "/artist")
public class ArtistController {

    private final ArtistApplicationService artistApplicationService;

    @Autowired
    public ArtistController(ArtistApplicationService artistApplicationService) {
        this.artistApplicationService = artistApplicationService;
    }

    @GetMapping(value = "/{id}")
    public ArtistDto getById(@PathVariable("id") Long id) {
        return artistApplicationService.getById(id);
    }

    @PostMapping
    public ArtistDto create(@RequestBody ArtistDto artistDto) {
        return artistApplicationService.create(artistDto);
    }

    @PutMapping
    public ArtistDto update(@RequestBody ArtistDto artistDto) {
        return artistApplicationService.update(artistDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteBy(@PathVariable("id") Long id) {
        artistApplicationService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
