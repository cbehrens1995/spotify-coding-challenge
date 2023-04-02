package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.cbehrens.spotifycodingchallenge.artist.ArtistBuilder;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRepository;
import org.cbehrens.spotifycodingchallenge.artist.ArtistSpotifyClient;
import org.cbehrens.spotifycodingchallenge.artist.ArtistUpdater;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScheduledArtistUpdaterTest {

    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private ArtistSpotifyClient artistSpotifyClient;
    @Mock
    private ArtistUpdater artistUpdater;

    private ScheduledArtistUpdater testee;

    @BeforeEach
    void init() {
        testee = new ScheduledArtistUpdater(artistRepository, artistSpotifyClient, artistUpdater);
        ReflectionTestUtils.setField(testee, "partitionSize", 2);
    }

    @Test
    void thatUpdateArtistWorks() {
        //given
        var artistSpotifyId1 = "1L";
        var artistSpotifyId2 = "2L";
        var artistSpotifyId3 = "3L";
        var artist1 = ArtistBuilder.artist(1L)
                .withSpotifyId(artistSpotifyId1)
                .build();
        var artist2 = ArtistBuilder.artist(2L)
                .withSpotifyId(artistSpotifyId2)
                .build();
        var artist3 = ArtistBuilder.artist(3L)
                .withSpotifyId(artistSpotifyId3)
                .build();
        var artistSpotifyDto1 = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withId(artistSpotifyId1)
                .build();
        var artistSpotifyDto2 = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withId(artistSpotifyId2)
                .build();
        var artistSpotifyDto3 = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withId(artistSpotifyId3)
                .build();
        var artistSpotifyWrapperDto1 = ArtistSpotifyWrapperDtoBuilder.artistSpotifyWrapperDto()
                .addArtistSpotifyDto(artistSpotifyDto1)
                .addArtistSpotifyDto(artistSpotifyDto2)
                .build();
        var artistSpotifyWrapperDto2 = ArtistSpotifyWrapperDtoBuilder.artistSpotifyWrapperDto()
                .addArtistSpotifyDto(artistSpotifyDto3)
                .build();

        when(artistRepository.findUnmodifiedSpotifyArtists())
                .thenReturn(List.of(artist1, artist2, artist3));
        when(artistSpotifyClient.getArtists(List.of(artistSpotifyId1, artistSpotifyId2)))
                .thenReturn(artistSpotifyWrapperDto1);
        when(artistSpotifyClient.getArtists(List.of(artistSpotifyId3)))
                .thenReturn(artistSpotifyWrapperDto2);

        //when
        testee.updateArtists();

        //then
        verify(artistUpdater).updateFromSpotify(artist1, artistSpotifyDto1);
        verify(artistUpdater).updateFromSpotify(artist2, artistSpotifyDto2);
        verify(artistUpdater).updateFromSpotify(artist3, artistSpotifyDto3);
    }
}