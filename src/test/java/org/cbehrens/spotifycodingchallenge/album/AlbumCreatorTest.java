package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDtoBuilder;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightType;
import org.cbehrens.spotifycodingchallenge.artist.ArtistBuilder;
import org.cbehrens.spotifycodingchallenge.artist.ArtistDtoBuilder;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRetriever;
import org.cbehrens.spotifycodingchallenge.artist.SpotifyBasedDtoValidator;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumCreatorTest {

    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private SpotifyBasedDtoValidator spotifyBasedDtoValidator;
    @Mock
    private ArtistRetriever artistRetriever;

    private AlbumCreator testee;

    @BeforeEach
    void init() {
        testee = new AlbumCreator(albumRepository, spotifyBasedDtoValidator, artistRetriever);
    }

    @Test
    void thatCreateManuallyWorks() {
        //given
        var id = 187L;
        var copyrightType = CopyrightType.P;
        var text = "text for copyright";
        var copyrightDto = CopyrightDtoBuilder.copyrightDto(null)
                .withCopyrightType(copyrightType)
                .withText(text)
                .build();
        var artist = ArtistBuilder.artist(2L).build();
        var artistDto = ArtistDtoBuilder.artistDto(2L).build();
        var externalSpotifyUrl = "externalSpotifyUrl";
        var spotifyId = "spotifyId";
        var uri = "uri";
        var trackCount = 5;
        var imageUrl = "imageUrl";
        var releaseDate = "2020-01";
        var releaseDatePrecision = ReleaseDatePrecision.MONTH;
        var restrictionReason = RestrictionReason.EXPLICIT;
        var albumType = AlbumType.SINGLE;
        var albumDto = AlbumDtoBuilder.albumDto(id)
                .withExternalSpotifyUrl(externalSpotifyUrl)
                .withSpotifyId(spotifyId)
                .withUri(uri)
                .withTrackCount(trackCount)
                .withImageUrl(imageUrl)
                .withReleaseDate(releaseDate)
                .withReleaseDatePrecision(releaseDatePrecision)
                .withRestrictionReason(restrictionReason)
                .withAlbumType(albumType)
                .addArtistDto(artistDto)
                .addCopyrightDto(copyrightDto)
                .build();

        when(artistRetriever.getOrCreate(artistDto))
                .thenReturn(artist);
        when(albumRepository.save(any(Album.class)))
                .thenAnswer(AdditionalAnswers.returnsFirstArg());

        //when
        Album result = testee.createManually(albumDto);

        //then
        assertThat(result)
                .returns(externalSpotifyUrl, Album::getExternalSpotifyUrl)
                .returns(spotifyId, Album::getSpotifyId)
                .returns(uri, Album::getUri)
                .returns(Origin.MANUAL, Album::getOrigin)
                .returns(false, Album::isManuallyAdjusted)
                .returns(trackCount, Album::getTrackCount)
                .returns(imageUrl, Album::getImageUrl)
                .returns(releaseDate, Album::getReleaseDate)
                .returns(releaseDatePrecision, Album::getReleaseDatePrecision)
                .returns(restrictionReason, Album::getRestrictionReason)
                .returns(albumType, Album::getAlbumType);

        assertThat(result.getArtists()).containsExactly(artist);
        assertThat(result.getCopyrights()).hasSize(1);
        assertThat(result.getCopyrights().get(0))
                .returns(copyrightType, Copyright::getCopyrightType)
                .returns(text, Copyright::getCopyrightText)
                .returns(result, Copyright::getAlbum);

        verify(spotifyBasedDtoValidator).assertDtoHasNoSpotifyInformation(albumDto);
    }
}