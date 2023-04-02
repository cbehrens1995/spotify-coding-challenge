package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightConvertService;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.artist.ArtistConvertService;
import org.cbehrens.spotifycodingchallenge.artist.ArtistDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AlbumConvertService {

    private final ArtistConvertService artistConvertService;
    private final CopyrightConvertService copyrightConvertService;

    @Autowired
    public AlbumConvertService(ArtistConvertService artistConvertService, CopyrightConvertService copyrightConvertService) {
        this.artistConvertService = artistConvertService;
        this.copyrightConvertService = copyrightConvertService;
    }

    public AlbumDto toDto(Album album) {
        List<Artist> artists = album.getArtists();
        List<ArtistDto> artistDtos = artistConvertService.toDtos(artists);

        List<Copyright> copyrights = album.getCopyrights();
        List<CopyrightDto> copyrightDtos = copyrightConvertService.toDtos(copyrights);

        return new AlbumDto(album.getId(), album.getExternalSpotifyUrl(), album.getSpotifyId(), album.getUri(), album.getOrigin(),
                album.isManuallyAdjusted(), album.getTrackCount(), album.getImageUrl(), album.getAlbumNname(), album.getReleaseDate(),
                album.getReleaseDatePrecision(), album.getRestrictionReason(), album.getAlbumType(), copyrightDtos, artistDtos);
    }

    public List<AlbumDto> toDtos(List<Album> albums) {
        return albums.stream()
                .map(this::toDto)
                .toList();
    }
}
