package org.cbehrens.spotifycodingchallenge.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistRetrieverTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private ArtistCreator artistCreator;

    private ArtistRetriever testee;

    @BeforeEach
    void init() {
        testee = new ArtistRetriever(artistRepository, artistCreator);
    }

    @Test
    void thatGetOrCreateWorks_Get() {
        //given
        var id = 1L;
        var artistDto = ArtistDtoBuilder.artistDto(id).build();
        var artist = ArtistBuilder.artist(id).build();

        when(artistRepository.findByIdOrThrow(id))
                .thenReturn(artist);

        //when
        Artist result = testee.getOrCreate(artistDto);

        //then
        assertThat(result).isEqualTo(artist);

        verifyNoInteractions(artistCreator);
    }

    @Test
    void thatGetOrCreateWorks_Create() {
        //given
        var id = 1L;
        var artistDto = ArtistDtoBuilder.artistDto(null).build();
        var artist = ArtistBuilder.artist(id).build();

        when(artistCreator.createManually(artistDto))
                .thenReturn(artist);

        //when
        Artist result = testee.getOrCreate(artistDto);

        //then
        assertThat(result).isEqualTo(artist);

        verifyNoInteractions(artistRepository);
    }
}