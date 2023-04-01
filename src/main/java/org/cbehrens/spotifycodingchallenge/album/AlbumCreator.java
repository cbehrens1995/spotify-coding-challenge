package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRetriever;
import org.cbehrens.spotifycodingchallenge.artist.SpotifyDtoValidator;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumCreator {

    private final AlbumRepository albumRepository;
    private final SpotifyDtoValidator spotifyDtoValidator;
    private final ArtistRetriever artistRetriever;

    @Autowired
    public AlbumCreator(AlbumRepository albumRepository, SpotifyDtoValidator spotifyDtoValidator, ArtistRetriever artistRetriever) {
        this.albumRepository = albumRepository;
        this.spotifyDtoValidator = spotifyDtoValidator;
        this.artistRetriever = artistRetriever;
    }

    public Album createManually(AlbumDto albumDto) {
        spotifyDtoValidator.assertDtoHasNoSpotifyInformation(albumDto);

        List<Artist> artists = albumDto.getArtistDtos().stream()
                .map(artistRetriever::getOrCreate)
                .toList();

        return createInternal(albumDto.getExternalSpotifyUrl(), albumDto.getSpotifyId(), albumDto.getUri(), Origin.MANUAL, albumDto.getTrackCount(),
                albumDto.getImageUrl(), albumDto.getName(), albumDto.getReleaseDate(), albumDto.getReleaseDatePrecision(),
                albumDto.getRestrictionReason(), albumDto.getAlbumType(), artists, albumDto.getCopyrightDtos());
    }

    private Album createInternal(String externalSpotifyUrl, String spotifyId, String uri, Origin origin, Integer trackCount,
                                 String imageUrl, String name, String releaseDate, ReleaseDatePrecision releaseDatePrecision,
                                 RestrictionReason restrictionReason, AlbumType albumType, List<Artist> artists, List<CopyrightDto> copyrightDtos) {
        Album album = new Album(externalSpotifyUrl, spotifyId, uri, origin, trackCount, imageUrl, name, releaseDate, releaseDatePrecision, restrictionReason, albumType);
        artists.forEach(album::addArtist);
        copyrightDtos.stream()
                .map(copyrightDto -> new Copyright(copyrightDto.getText(), copyrightDto.getCopyrightType(), album))
                .forEach(album::addCopyright);
        return albumRepository.save(album);
    }
}
