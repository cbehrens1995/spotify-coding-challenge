package org.cbehrens.spotifycodingchallenge.artist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArtistApplicationService {

    private final ArtistCreator artistCreator;
    private final ArtistRepository artistRepository;
    private final ArtistConvertService artistConvertService;
    private final ArtistUpdater artistUpdater;

    @Autowired
    public ArtistApplicationService(ArtistCreator artistCreator, ArtistRepository artistRepository, ArtistConvertService artistConvertService, ArtistUpdater artistUpdater) {
        this.artistCreator = artistCreator;
        this.artistRepository = artistRepository;
        this.artistConvertService = artistConvertService;
        this.artistUpdater = artistUpdater;
    }

    public ArtistDto getById(Long id) {
        Artist artist = artistRepository.findByIdOrThrow(id);
        return artistConvertService.toDto(artist);
    }

    public ArtistDto create(ArtistDto artistDto) {
        Artist artist = artistCreator.createManually(artistDto);
        return artistConvertService.toDto(artist);
    }

    public ArtistDto update(ArtistDto artistDto) {
        Artist artist = artistRepository.findByIdOrThrow(artistDto.getId());
        Artist updatedArtist = artistUpdater.updateManually(artist, artistDto);
        return artistConvertService.toDto(updatedArtist);
    }

    public void deleteById(Long id) {
        Artist artist = artistRepository.findByIdOrThrow(id);
        artistRepository.delete(artist);
    }
}
