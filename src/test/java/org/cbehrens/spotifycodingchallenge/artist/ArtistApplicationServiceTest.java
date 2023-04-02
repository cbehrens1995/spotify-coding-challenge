package org.cbehrens.spotifycodingchallenge.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistApplicationServiceTest {

    @Mock
    private ArtistCreator artistCreator;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private ArtistConvertService artistConvertService;
    @Mock
    private ArtistUpdater artistUpdater;

    private ArtistApplicationService testee;

    @BeforeEach
    void init() {
        testee = new ArtistApplicationService(artistCreator, artistRepository, artistConvertService, artistUpdater);
    }

    @Test
    void thatGetByIdWorks() {
        //given
        var id = 187L;
        var artist = ArtistBuilder.artist(id).build();
        var artistDto = ArtistDtoBuilder.artistDto(null).build();

        when(artistRepository.findByIdOrThrow(id))
                .thenReturn(artist);
        when(artistConvertService.toDto(artist))
                .thenReturn(artistDto);

        //when
        ArtistDto result = testee.getById(id);

        //then
        assertThat(result).isEqualTo(artistDto);
    }

    @Test
    void thatCreateWorks() {
        //given
        var artistDto1 = ArtistDtoBuilder.artistDto(null).build();
        var artist = ArtistBuilder.artist(1L).build();
        var artistDto2 = ArtistDtoBuilder.artistDto(null).build();

        when(artistCreator.createManually(artistDto1))
                .thenReturn(artist);
        when(artistConvertService.toDto(artist))
                .thenReturn(artistDto2);

        //when
        ArtistDto result = testee.create(artistDto1);

        //then
        assertThat(result).isEqualTo(artistDto2);
    }

    @Test
    void thatDeleteByIdWorks() {
        //given
        var id = 187L;
        var artist = ArtistBuilder.artist(id).build();

        when(artistRepository.findByIdOrThrow(id))
                .thenReturn(artist);

        //when
        testee.deleteById(id);

        //then
        verify(artistRepository).delete(artist);
    }

    @Test
    void thatUpdateWorks() {
        //given
        var id = 1337L;
        var artistDto = ArtistDtoBuilder.artistDto(id).build();
        var artist = ArtistBuilder.artist(id).build();
        var updatedArtist = ArtistBuilder.artist(id).build();
        var resultDto = ArtistDtoBuilder.artistDto(null).build();

        when(artistRepository.findByIdOrThrow(id))
                .thenReturn(artist);
        when(artistUpdater.updateFromSpotify(artist, artistDto))
                .thenReturn(updatedArtist);
        when(artistConvertService.toDto(updatedArtist))
                .thenReturn(resultDto);

        //when
        ArtistDto result = testee.update(artistDto);

        //then
        assertThat(result).isEqualTo(resultDto);
    }
}