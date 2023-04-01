package org.cbehrens.spotifycodingchallenge.album;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/album")
public class AlbumController {

    private final AlbumApplicationService albumApplicationService;

    @Autowired
    public AlbumController(AlbumApplicationService albumApplicationService) {
        this.albumApplicationService = albumApplicationService;
    }

    @GetMapping(value = "/{id}")
    public AlbumDto getById(@PathVariable("id") Long id) {
        return albumApplicationService.getById(id);
    }

    @GetMapping(value = "/byArtist/{artistId}")
    public List<AlbumDto> getByArtist(@PathVariable("artistId") Long artistId) {
        return albumApplicationService.getByArtist(artistId);
    }

    @PostMapping
    public AlbumDto create(@RequestBody AlbumDto albumDto) {
        return albumApplicationService.create(albumDto);
    }

    @PutMapping
    public AlbumDto update(@RequestBody AlbumDto albumDto) {
        return albumApplicationService.update(albumDto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Long> deleteBy(@PathVariable("id") Long id) {
        albumApplicationService.deleteById(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
