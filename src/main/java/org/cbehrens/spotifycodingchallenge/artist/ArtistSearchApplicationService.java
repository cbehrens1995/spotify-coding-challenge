package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArtistSearchApplicationService {

    private final ArtistIndexService artistIndexService;
    private final ArtistConvertService artistConvertService;

    @Autowired
    public ArtistSearchApplicationService(ArtistIndexService artistIndexService, ArtistConvertService artistConvertService) {
        this.artistIndexService = artistIndexService;
        this.artistConvertService = artistConvertService;
    }

    public List<ArtistDto> search(String search, Integer maxResult) {
        List<Artist> artists = artistIndexService.searchInIndex(search.toLowerCase(), maxResult);
        return artistConvertService.toDtos(artists);
    }
}
