package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightBuilder;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightConvertService;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDtoBuilder;
import org.cbehrens.spotifycodingchallenge.artist.ArtistBuilder;
import org.cbehrens.spotifycodingchallenge.artist.ArtistConvertService;
import org.cbehrens.spotifycodingchallenge.artist.ArtistDtoBuilder;
import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumConvertServiceTest {

    @Mock
    private ArtistConvertService artistConvertService;
    @Mock
    private CopyrightConvertService copyrightConvertService;

    private AlbumConvertService testee;

    @BeforeEach
    void init() {
        testee = new AlbumConvertService(artistConvertService, copyrightConvertService);
    }

    @Test
    void thatToDtosWorks() {
        //given
        var id1 = 1L;
        var id2 = 12L;
        var album1 = AlbumBuilder.album(id1).build();
        var album2 = AlbumBuilder.album(id2).build();

        when(copyrightConvertService.toDtos(List.of()))
                .thenReturn(List.of());
        when(artistConvertService.toDtos(List.of()))
                .thenReturn(List.of());

        //when
        List<AlbumDto> result = testee.toDtos(List.of(album1, album2));

        //then
        assertThat(result).extracting(
                AbstractDto::getId
        ).containsExactly(
                id1, id2
        );
    }

    @Test
    void thatToDtoWorks() {
        //given
        var id = 187L;
        var copyright = CopyrightBuilder.copyright(1L).build();
        var copyrightDto = CopyrightDtoBuilder.copyrightDto(1L).build();
        var artist = ArtistBuilder.artist(2L).build();
        var artistDto = ArtistDtoBuilder.artistDto(2L).build();
        var externalSpotifyUrl = "externalSpotifyUrl";
        var spotifyId = "spotifyId";
        var uri = "uri";
        var origin = Origin.MANUAL;
        var manuallyAdjusted = true;
        var trackCount = 5;
        var imageUrl = "imageUrl";
        var releaseDate = "2020-01";
        var releaseDatePrecision = ReleaseDatePrecision.MONTH;
        var restrictionReason = RestrictionReason.EXPLICIT;
        var albumType = AlbumType.SINGLE;
        var album = AlbumBuilder.album(id)
                .withExternalSpotifyUrl(externalSpotifyUrl)
                .withSpotifyId(spotifyId)
                .withUri(uri)
                .withOrigin(origin)
                .withManuallyAdjusted(manuallyAdjusted)
                .withTrackCount(trackCount)
                .withImageUrl(imageUrl)
                .withReleaseDate(releaseDate)
                .withReleaseDatePrecision(releaseDatePrecision)
                .withRestrictionReason(restrictionReason)
                .withAlbumType(albumType)
                .addArtist(artist)
                .addCopyright(copyright)
                .build();

        when(copyrightConvertService.toDtos(List.of(copyright)))
                .thenReturn(List.of(copyrightDto));
        when(artistConvertService.toDtos(List.of(artist)))
                .thenReturn(List.of(artistDto));

        //when
        AlbumDto result = testee.toDto(album);

        //then
        assertThat(result)
                .returns(externalSpotifyUrl, AlbumDto::getExternalSpotifyUrl)
                .returns(spotifyId, AlbumDto::getSpotifyId)
                .returns(uri, AlbumDto::getUri)
                .returns(origin, AlbumDto::getOrigin)
                .returns(manuallyAdjusted, AlbumDto::getManuallyAdjusted)
                .returns(trackCount, AlbumDto::getTrackCount)
                .returns(imageUrl, AlbumDto::getImageUrl)
                .returns(releaseDate, AlbumDto::getReleaseDate)
                .returns(releaseDatePrecision, AlbumDto::getReleaseDatePrecision)
                .returns(restrictionReason, AlbumDto::getRestrictionReason)
                .returns(albumType, AlbumDto::getAlbumType)
                .returns(id, AbstractDto::getId);

        assertThat(result.getCopyrightDtos()).containsExactly(copyrightDto);
        assertThat(result.getArtistDtos()).containsExactly(artistDto);
    }
}