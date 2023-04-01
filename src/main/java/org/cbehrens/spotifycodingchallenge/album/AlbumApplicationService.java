package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AlbumApplicationService {

    private final AlbumCreator albumCreator;
    private final AlbumRepository albumRepository;
    private final AlbumConvertService albumConvertService;
    private final AlbumUpdater albumUpdater;
    private final ArtistRepository artistRepository;

    @Autowired
    public AlbumApplicationService(AlbumCreator albumCreator, AlbumRepository albumRepository, AlbumConvertService albumConvertService, AlbumUpdater albumUpdater, ArtistRepository artistRepository) {
        this.albumCreator = albumCreator;
        this.albumRepository = albumRepository;
        this.albumConvertService = albumConvertService;
        this.albumUpdater = albumUpdater;
        this.artistRepository = artistRepository;
    }

    @Transactional(readOnly = true)
    public AlbumDto getById(Long id) {
        Album album = albumRepository.findByIdOrThrow(id);
        return albumConvertService.toDto(album);
    }

    @Transactional(readOnly = true)
    public List<AlbumDto> getByArtist(Long artistId) {
        Artist artist = artistRepository.findByIdOrThrow(artistId);
        List<Album> albums = artist.getAlbums();
        return albumConvertService.toDtos(albums);
    }

    @Transactional
    public AlbumDto create(AlbumDto albumDto) {
        Album album = albumCreator.createManually(albumDto);
        return albumConvertService.toDto(album);
    }

    @Transactional
    public AlbumDto update(AlbumDto albumDto) {
        Album album = albumRepository.findByIdOrThrow(albumDto.getId());
        Album updatedAlbum = albumUpdater.update(album, albumDto);
        return albumConvertService.toDto(updatedAlbum);
    }

    public void deleteById(Long id) {
        Album album = albumRepository.findByIdOrThrow(id);
        albumRepository.delete(album);
    }
}
