package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.cbehrens.spotifycodingchallenge.artist.ArtistCreator;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRepository;
import org.cbehrens.spotifycodingchallenge.artist.ArtistSpotifyClient;
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
class ArtistDataInitializerTest {

    public static final List<String> ARTIST_IDS = List.of("HSV1887", "HSV1887222");
    @Mock
    private ArtistSpotifyClient artistSpotifyClient;
    @Mock
    private ArtistCreator artistCreator;
    @Mock
    private ArtistRepository artistRepository;

    private ArtistDataInitializer testee;

    @BeforeEach
    void init() {
        testee = new ArtistDataInitializer(artistSpotifyClient, artistCreator, artistRepository);
        ReflectionTestUtils.setField(testee, "artistSpotifyIds", ARTIST_IDS);
    }

    @Test
    void thatInitArtistsWorks() {
        //given
        var spotifyId1 = "spotifyId1";
        var artistSpotifyDto1 = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withId(spotifyId1)
                .build();
        var spotifyId2 = "spotifyId2";
        var artistSpotifyDto2 = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withId(spotifyId2)
                .build();
        var artistSpotifyWrapperDto = ArtistSpotifyWrapperDtoBuilder.artistSpotifyWrapperDto()
                .addArtistSpotifyDto(artistSpotifyDto1)
                .addArtistSpotifyDto(artistSpotifyDto2)
                .build();

        when(artistSpotifyClient.getArtists(ARTIST_IDS))
                .thenReturn(artistSpotifyWrapperDto);
        when(artistRepository.existsBySpotifyId(spotifyId1))
                .thenReturn(false);
        when(artistRepository.existsBySpotifyId(spotifyId2))
                .thenReturn(true);

        //when
        testee.initArtists();

        //then
        verify(artistCreator).createFromSpotify(artistSpotifyDto1);
    }
}