package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightBuilder;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDtoBuilder;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightType;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumSpotifyDtoBuilder;
import org.cbehrens.spotifycodingchallenge.artist.*;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDtoBuilder;
import org.cbehrens.spotifycodingchallenge.commons.AbstractSpotifyEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumUpdaterTest {

    @Mock
    private SpotifyDtoValidator spotifyDtoValidator;
    @Mock
    private ArtistRetriever artistRetriever;
    @Mock
    private ArtistRepository artistRepository;

    private AlbumUpdater testee;

    @BeforeEach
    void init() {
        testee = new AlbumUpdater(spotifyDtoValidator, artistRetriever, artistRepository);
    }

    private static Stream<Arguments> parameterFor_thatUpdateWorks_UpdateOfFields() {
        return Stream.of(
                Arguments.of(1, null, null, null, null, null, null),
                Arguments.of(null, "ImageUrl", null, null, null, null, null),
                Arguments.of(null, null, "name", null, null, null, null),
                Arguments.of(null, null, null, "2020-10", null, null, null),
                Arguments.of(null, null, null, null, ReleaseDatePrecision.DAY, null, null),
                Arguments.of(null, null, null, null, null, RestrictionReason.MARKET, null),
                Arguments.of(null, null, null, null, null, null, AlbumType.ALBUM)
        );
    }

    @ParameterizedTest
    @MethodSource("parameterFor_thatUpdateWorks_UpdateOfFields")
    void thatUpdateWorks_UpdateOfFields(Integer trackCount,
                                        String imageUrl,
                                        String name,
                                        String releaseDate,
                                        ReleaseDatePrecision releaseDatePrecision,
                                        RestrictionReason restrictionReason,
                                        AlbumType albumType) {
        //given
        var album = AlbumBuilder.album(1L).build();
        var albumDto = AlbumDtoBuilder.albumDto(1L)
                .withTrackCount(trackCount)
                .withImageUrl(imageUrl)
                .withName(name)
                .withReleaseDate(releaseDate)
                .withReleaseDatePrecision(releaseDatePrecision)
                .withRestrictionReason(restrictionReason)
                .withAlbumType(albumType)
                .build();

        when(artistRepository.findAllByIdOrThrow(List.of()))
                .thenReturn(List.of());

        //when
        Album result = testee.update(album, albumDto);

        //then
        assertThat(result)
                .returns(trackCount, Album::getTrackCount)
                .returns(imageUrl, Album::getImageUrl)
                .returns(name, Album::getName)
                .returns(releaseDate, Album::getReleaseDate)
                .returns(releaseDatePrecision, Album::getReleaseDatePrecision)
                .returns(restrictionReason, Album::getRestrictionReason)
                .returns(albumType, Album::getAlbumType)
                .returns(true, AbstractSpotifyEntity::isManuallyAdjusted);

        verify(spotifyDtoValidator).assertDtoHasNoSpotifyInformation(albumDto);
    }

    @Test
    void thatUpdateWorks_NothingChanged() {
        //given
        var text = "test for copyright";
        var copyrightType = CopyrightType.P;
        var copyrightDto = CopyrightDtoBuilder.copyrightDto(null)
                .withText(text)
                .withCopyrightType(copyrightType)
                .build();
        var copyright = CopyrightBuilder.copyright(12L)
                .withCopyrightType(copyrightType)
                .withText(text)
                .build();

        var artistId = 13L;
        var artistDto = ArtistDtoBuilder.artistDto(artistId).build();
        var artist = ArtistBuilder.artist(artistId).build();

        var albumDto = AlbumDtoBuilder.albumDto(1L)
                .addCopyrightDto(copyrightDto)
                .addArtistDto(artistDto)
                .build();
        var album = AlbumBuilder.album(1L)
                .addCopyright(copyright)
                .addArtist(artist)
                .build();

        when(artistRepository.findAllByIdOrThrow(List.of(artistId)))
                .thenReturn(List.of(artist));

        //when
        Album result = testee.update(album, albumDto);

        //then
        assertThat(result.isManuallyAdjusted()).isFalse();

        verify(spotifyDtoValidator).assertDtoHasNoSpotifyInformation(albumDto);
        verifyNoInteractions(artistRetriever);
    }

    @Test
    void thatUpdateWorks_Copyright() {
        //given
        var text = "test for copyright";
        var copyrightType = CopyrightType.P;
        var copyrightDto = CopyrightDtoBuilder.copyrightDto(null)
                .withText(text)
                .withCopyrightType(copyrightType)
                .build();
        var copyright1 = CopyrightBuilder.copyright(12L)
                .withCopyrightType(copyrightType)
                .withText(text)
                .build();
        var copyright2 = CopyrightBuilder.copyright(12L)
                .withCopyrightType(CopyrightType.C)
                .withText("new text")
                .build();

        var albumDto = AlbumDtoBuilder.albumDto(1L)
                .addCopyrightDto(copyrightDto)
                .build();
        var album = AlbumBuilder.album(1L)
                .addCopyright(copyright1)
                .addCopyright(copyright2)
                .build();

        when(artistRepository.findAllByIdOrThrow(List.of()))
                .thenReturn(List.of());

        //when
        Album result = testee.update(album, albumDto);

        //then
        assertThat(result.isManuallyAdjusted()).isTrue();
        assertThat(result.getCopyrights()).hasSize(1);
        assertThat(result.getCopyrights().get(0))
                .returns(text, Copyright::getText)
                .returns(copyrightType, Copyright::getCopyrightType)
                .returns(album, Copyright::getAlbum);

        verify(spotifyDtoValidator).assertDtoHasNoSpotifyInformation(albumDto);
        verifyNoInteractions(artistRetriever);
    }

    @Test
    void thatUpdateWorks_Artist_BrandNewArtist() {
        //given
        var artistDto = ArtistDtoBuilder.artistDto(null).build();
        var artist1 = ArtistBuilder.artist(1337L).build();
        var artist2 = ArtistBuilder.artist(2L).build();

        var albumDto = AlbumDtoBuilder.albumDto(1L)
                .addArtistDto(artistDto)
                .build();
        var album = AlbumBuilder.album(1L)
                .addArtist(artist1)
                .addArtist(artist2)
                .build();

        when(artistRetriever.getOrCreate(artistDto))
                .thenReturn(artist1);

        //when
        Album result = testee.update(album, albumDto);

        //then
        assertThat(result.isManuallyAdjusted()).isTrue();
        assertThat(result.getArtists()).containsExactly(artist1);

        verify(spotifyDtoValidator).assertDtoHasNoSpotifyInformation(albumDto);
        verifyNoInteractions(artistRepository);
    }

    /***
     * In this case we are using already existing artists which should be added to the album
     * */
    @Test
    void thatUpdateWorks_Artists() {
        //given
        var artistId = 1337L;
        var artistDto = ArtistDtoBuilder.artistDto(artistId).build();
        var artist = ArtistBuilder.artist(artistId).build();
        var artist2 = ArtistBuilder.artist(2L).build();

        var albumDto = AlbumDtoBuilder.albumDto(1L)
                .addArtistDto(artistDto)
                .build();
        var album = AlbumBuilder.album(1L)
                .addArtist(artist)
                .addArtist(artist2)
                .build();

        when(artistRepository.findAllByIdOrThrow(List.of(artistId)))
                .thenReturn(List.of(artist));

        //when
        Album result = testee.update(album, albumDto);

        //then
        assertThat(result.isManuallyAdjusted()).isTrue();
        assertThat(result.getArtists()).containsExactly(artist);

        verify(spotifyDtoValidator).assertDtoHasNoSpotifyInformation(albumDto);
        verifyNoInteractions(artistRetriever);
    }

    @Test
    void thatUpdateWorks_FromSpotify() {
        //given
        var artist1 = ArtistBuilder.artist(2L).build();
        var album = AlbumBuilder.album(1L)
                .addArtist(artist1)
                .build();
        var imageUrl = "imageUrl";
        var artistSpotifyDto = ArtistSpotifyDtoBuilder.artistSpotifyDto().build();
        var albumSpotifyDto = AlbumSpotifyDtoBuilder.albumSpotifyDto()
                .addImageUrl(imageUrl)
                .withRestrictionReason(null)
                .addArtistSpotifyDto(artistSpotifyDto)
                .build();
        var artist2 = ArtistBuilder.artist(3L).build();

        when(artistRetriever.getOrCreateBySpotify(artistSpotifyDto))
                .thenReturn(artist2);

        //when
        testee.update(album, albumSpotifyDto);

        //then
        assertThat(album)
                .returns(imageUrl, Album::getImageUrl)
                .returns(null, Album::getRestrictionReason);
        assertThat(album.getArtists()).containsExactly(artist2);
    }
}