package org.cbehrens.spotifycodingchallenge.artist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ArtistSearchApplicationServiceTest {

    @Mock
    private ArtistIndexService artistIndexService;
    @Mock
    private ArtistConvertService artistConvertService;

    private ArtistSearchApplicationService testee;

    @BeforeEach
    void init() {
        testee = new ArtistSearchApplicationService(artistIndexService, artistConvertService);
    }

    @Test
    void thatSearchWorks() {
        //given
        var searchString = "star search";
        var maxResult = 10;
        var artist = ArtistBuilder.artist(1L).build();
        var artistDto = ArtistDtoBuilder.artistDto(1L).build();

        when(artistIndexService.searchInIndex(searchString, maxResult))
                .thenReturn(List.of(artist));
        when(artistConvertService.toDtos(List.of(artist)))
                .thenReturn(List.of(artistDto));

        //when
        List<ArtistDto> result = testee.search(searchString, maxResult);

        //then
        assertThat(result).containsExactly(artistDto);
    }
}