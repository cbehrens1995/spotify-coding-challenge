package org.cbehrens.spotifycodingchallenge.artist.spotify;

import org.cbehrens.spotifycodingchallenge.album.*;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumByArtistSpotifyDtoBuilder;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumSpotifyDtoBuilder;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumsByArtistSpotifyDtoBuilder;
import org.cbehrens.spotifycodingchallenge.artist.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArtistDataInitializerTest {

    private static final List<String> ARTIST_IDS = List.of("HSV1887", "HSV1887222");
    private static final List<String> INCLUDED_GROUPS = List.of("DE");
    private static final String MARKET = "DE";
    private static final Integer OFFSET = 1;
    private static final Integer LIMIT = 10;

    @Mock
    private ArtistSpotifyClient artistSpotifyClient;
    @Mock
    private ArtistCreator artistCreator;
    @Mock
    private ArtistRepository artistRepository;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private AlbumCreator albumCreator;
    @Mock
    private AlbumSpotifyClient albumSpotifyClient;
    @Mock
    private AlbumUpdater albumUpdater;
    @Mock
    private ArtistIndexService artistIndexService;

    private ArtistDataInitializer testee;

    @BeforeEach
    void init() {
        testee = new ArtistDataInitializer(artistSpotifyClient, artistCreator, artistRepository, albumRepository, albumCreator, albumSpotifyClient, albumUpdater, artistIndexService);
        ReflectionTestUtils.setField(testee, "artistSpotifyIds", ARTIST_IDS);
        ReflectionTestUtils.setField(testee, "includedGroups", INCLUDED_GROUPS);
        ReflectionTestUtils.setField(testee, "market", MARKET);
        ReflectionTestUtils.setField(testee, "offset", OFFSET);
        ReflectionTestUtils.setField(testee, "limit", LIMIT);
    }

    @Test
    void thatInitArtistsWorks() throws InterruptedException {
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
        var artist = ArtistBuilder.artist(1L)
                .withSpotifyId(spotifyId1)
                .build();
        var albumsByArtistSpotifyDto = AlbumsByArtistSpotifyDtoBuilder.albumsByArtistSpotifyDto().build();

        when(artistSpotifyClient.getArtists(ARTIST_IDS))
                .thenReturn(artistSpotifyWrapperDto);
        when(artistRepository.existsBySpotifyId(spotifyId1))
                .thenReturn(false);
        when(artistRepository.existsBySpotifyId(spotifyId2))
                .thenReturn(true);
        when(artistCreator.createFromSpotify(artistSpotifyDto1))
                .thenReturn(artist);
        when(artistSpotifyClient.getAlbumsByArtist(spotifyId1, INCLUDED_GROUPS, MARKET, LIMIT, OFFSET))
                .thenReturn(albumsByArtistSpotifyDto);

        //when
        testee.initArtists();

        //then
        verify(artistIndexService).initiateIndexing();
        verifyNoInteractions(albumRepository);
        verifyNoMoreInteractions(artistSpotifyClient, albumCreator, albumUpdater);
    }

    @Test
    void thatInitArtistsWorks_UpdateAlbums() throws InterruptedException {
        //given
        var spotifyId1 = "spotifyId1";
        var artistSpotifyDto1 = ArtistSpotifyDtoBuilder.artistSpotifyDto()
                .withId(spotifyId1)
                .build();
        var artistSpotifyWrapperDto = ArtistSpotifyWrapperDtoBuilder.artistSpotifyWrapperDto()
                .addArtistSpotifyDto(artistSpotifyDto1)
                .build();
        var artist = ArtistBuilder.artist(1L)
                .withSpotifyId(spotifyId1)
                .build();
        var albumSpotifyId = "albumSpotifyId";
        var albumByArtistSpotifyDto = AlbumByArtistSpotifyDtoBuilder.albumByArtistSpotifyDto()
                .withId(albumSpotifyId)
                .build();
        var albumsByArtistSpotifyDto = AlbumsByArtistSpotifyDtoBuilder.albumsByArtistSpotifyDto()
                .addAlbumByArtistSpotifyDto(albumByArtistSpotifyDto)
                .build();
        var albumSpotifyDto = AlbumSpotifyDtoBuilder.albumSpotifyDto().build();
        var album = AlbumBuilder.album(187L).build();

        when(artistSpotifyClient.getArtists(ARTIST_IDS))
                .thenReturn(artistSpotifyWrapperDto);
        when(artistRepository.existsBySpotifyId(spotifyId1))
                .thenReturn(false);
        when(artistCreator.createFromSpotify(artistSpotifyDto1))
                .thenReturn(artist);
        when(artistSpotifyClient.getAlbumsByArtist(spotifyId1, INCLUDED_GROUPS, MARKET, LIMIT, OFFSET))
                .thenReturn(albumsByArtistSpotifyDto);
        when(albumRepository.existsBySpotifyId(albumSpotifyId))
                .thenReturn(true);
        when(albumRepository.existsBySpotifyIdAndNotArtist(albumSpotifyId, artist))
                .thenReturn(true);
        when(albumSpotifyClient.getAlbum(albumSpotifyId))
                .thenReturn(albumSpotifyDto);
        when(albumRepository.findBySpotifyId(albumSpotifyId))
                .thenReturn(album);

        //when
        testee.initArtists();

        //then
        verify(albumUpdater).update(album, albumSpotifyDto);
        verify(artistIndexService).initiateIndexing();
    }

    @Test
    void thatCreateNewAlbumForArtistWorks() {
        //given
        var artist = ArtistBuilder.artist(1L).build();
        var artistSpotifyId1 = "artistSpotifyId1";
        var artistSpotifyId2 = "artistSpotifyId2";
        var artistSpotifyId3 = "artistSpotifyId3";
        var artistShortSpotifyDto1 = ArtistShortSpotifyDtoBuilder.artistShortSpotifyDto()
                .withId(artistSpotifyId1)
                .build();
        var artistShortSpotifyDto2 = ArtistShortSpotifyDtoBuilder.artistShortSpotifyDto()
                .withId(artistSpotifyId2)
                .build();
        var artistShortSpotifyDto3 = ArtistShortSpotifyDtoBuilder.artistShortSpotifyDto()
                .withId(artistSpotifyId3)
                .build();
        var albumSpotifyId1 = "albumSpotifyId1";
        var albumSpotifyId2 = "albumSpotifyId2";
        var albumByArtistSpotifyDto1 = AlbumByArtistSpotifyDtoBuilder.albumByArtistSpotifyDto()
                .addArtistShortSpotifyDto(artistShortSpotifyDto1)
                .addArtistShortSpotifyDto(artistShortSpotifyDto2)
                .withId(albumSpotifyId1)
                .build();
        var albumByArtistSpotifyDto2 = AlbumByArtistSpotifyDtoBuilder.albumByArtistSpotifyDto()
                .addArtistShortSpotifyDto(artistShortSpotifyDto3)
                .withId(albumSpotifyId2)
                .build();
        var artistSpotifyDto1 = ArtistSpotifyDtoBuilder.artistSpotifyDto().build();
        var artistSpotifyDto2 = ArtistSpotifyDtoBuilder.artistSpotifyDto().build();
        var artistSpotifyWrapperDto = ArtistSpotifyWrapperDtoBuilder.artistSpotifyWrapperDto()
                .addArtistSpotifyDto(artistSpotifyDto1)
                .addArtistSpotifyDto(artistSpotifyDto2)
                .build();

        when(albumRepository.existsBySpotifyId(albumSpotifyId1))
                .thenReturn(false);
        when(albumRepository.existsBySpotifyId(albumSpotifyId2))
                .thenReturn(true);
        when(artistSpotifyClient.getArtists(List.of(artistSpotifyId1, artistSpotifyId2)))
                .thenReturn(artistSpotifyWrapperDto);
        //when
        testee.createNewAlbumsForArtist(artist, List.of(albumByArtistSpotifyDto1, albumByArtistSpotifyDto2));

        //then
        verify(albumCreator).create(albumByArtistSpotifyDto1, artistSpotifyWrapperDto.artistSpotifyDtos());
    }
}