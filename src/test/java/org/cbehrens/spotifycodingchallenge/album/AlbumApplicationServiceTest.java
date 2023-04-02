package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.artist.ArtistBuilder;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumApplicationServiceTest {

    @Mock
    private AlbumCreator albumCreator;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private AlbumConvertService albumConvertService;
    @Mock
    private AlbumUpdater albumUpdater;
    @Mock
    private ArtistRepository artistRepository;

    private AlbumApplicationService testee;

    @BeforeEach
    void init() {
        testee = new AlbumApplicationService(albumCreator, albumRepository, albumConvertService, albumUpdater, artistRepository);
    }

    @Test
    void thatGetByIdWorks() {
        //given
        var id = 1887L;
        var album = AlbumBuilder.album(id).build();
        var albumDto = AlbumDtoBuilder.albumDto(id).build();

        when(albumRepository.findByIdOrThrow(id))
                .thenReturn(album);
        when(albumConvertService.toDto(album))
                .thenReturn(albumDto);

        //when
        AlbumDto result = testee.getById(id);

        //then
        assertThat(result).isEqualTo(albumDto);
    }


    @Test
    void thatGetByArtistIdWorks() {
        //given
        var id = 1887L;
        var album = AlbumBuilder.album(id).build();
        var artist = ArtistBuilder.artist(333L)
                .addAlbum(album)
                .build();
        var albumDto = AlbumDtoBuilder.albumDto(id).build();

        when(artistRepository.findByIdOrThrow(artist.getId()))
                .thenReturn(artist);
        when(albumConvertService.toDtos(List.of(album)))
                .thenReturn(List.of(albumDto));

        //when
        List<AlbumDto> result = testee.getByArtist(artist.getId());

        //then
        assertThat(result).containsExactly(albumDto);
    }

    @Test
    void thatCreateWorks() {
        //given
        var albumDto = AlbumDtoBuilder.albumDto(null).build();
        var album = AlbumBuilder.album(1L).build();
        var newAlbumDto = AlbumDtoBuilder.albumDto(null).build();

        when(albumCreator.createManually(albumDto))
                .thenReturn(album);
        when(albumConvertService.toDto(album))
                .thenReturn(newAlbumDto);

        //when
        AlbumDto result = testee.create(albumDto);

        //then
        assertThat(result).isEqualTo(newAlbumDto);
    }

    @Test
    void thatUpdateWorks() {
        //given
        var id = 187L;
        var albumDto = AlbumDtoBuilder.albumDto(id).build();
        var album = AlbumBuilder.album(id).build();
        var updatedAlbum = AlbumBuilder.album(id).build();
        var newAlbumDto = AlbumDtoBuilder.albumDto(id).build();

        when(albumRepository.findByIdOrThrow(id))
                .thenReturn(album);
        when(albumUpdater.update(album, albumDto))
                .thenReturn(updatedAlbum);
        when(albumConvertService.toDto(updatedAlbum))
                .thenReturn(newAlbumDto);

        //when
        AlbumDto result = testee.update(albumDto);

        //then
        assertThat(result).isEqualTo(newAlbumDto);
    }

    @Test
    void thatDeleteByIdWorks() {
        //given
        var id = 187L;
        var albumDto = AlbumDtoBuilder.albumDto(id).build();
        var album = AlbumBuilder.album(id).build();

        when(albumRepository.findByIdOrThrow(id))
                .thenReturn(album);

        //when
        assertThatNoException().isThrownBy(() -> testee.deleteById(id));

        //then
        verify(albumRepository).delete(album);
    }
}