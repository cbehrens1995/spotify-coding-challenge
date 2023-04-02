package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/artist/search")
public class ArtistSearchController {

    private final ArtistSearchApplicationService artistSearchApplicationService;

    @Autowired
    public ArtistSearchController(ArtistSearchApplicationService artistSearchApplicationService) {
        this.artistSearchApplicationService = artistSearchApplicationService;
    }

    @GetMapping
    public List<ArtistDto> search(@RequestParam("searchString") String search,
                                  @RequestParam(value = "maxResult", defaultValue = "1") Integer maxResult) {
        return artistSearchApplicationService.search(search, maxResult);
    }
}
