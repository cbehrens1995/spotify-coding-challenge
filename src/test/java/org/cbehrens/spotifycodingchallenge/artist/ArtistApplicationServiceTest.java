package org.cbehrens.spotifycodingchallenge.artist;

import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
        var artist = mock(Artist.class);
        var artistDto = createDtoWithOnlyNullValues();
        var id = 187L;

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
        var artistDto1 = createDtoWithOnlyNullValues();
        var artist = mock(Artist.class);
        var artistDto2 = createDtoWithOnlyNullValues();

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
        var artist = mock(Artist.class);
        var id = 187L;

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
        var artistDto = new ArtistDto(id, null, null, null, null, null, null, null, Origin.MANUAL, false);
        var artist = mock(Artist.class);
        var updatedArtist = mock(Artist.class);
        var resultDto = createDtoWithOnlyNullValues();

        when(artistRepository.findByIdOrThrow(id))
                .thenReturn(artist);
        when(artistUpdater.update(artist, artistDto))
                .thenReturn(updatedArtist);
        when(artistConvertService.toDto(updatedArtist))
                .thenReturn(resultDto);

        //when
        ArtistDto result = testee.update(artistDto);

        //then
        assertThat(result).isEqualTo(resultDto);
    }

    private ArtistDto createDtoWithOnlyNullValues() {
        return new ArtistDto(null, null, null, null, null, null, null, null, Origin.MANUAL, false);
    }
}