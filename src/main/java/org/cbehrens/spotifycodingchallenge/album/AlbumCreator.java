package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumByArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.CopyrightSpotifyDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.Restriction;
import org.cbehrens.spotifycodingchallenge.artist.Artist;
import org.cbehrens.spotifycodingchallenge.artist.ArtistRetriever;
import org.cbehrens.spotifycodingchallenge.artist.SpotifyDtoValidator;
import org.cbehrens.spotifycodingchallenge.artist.spotify.ArtistSpotifyDto;
import org.cbehrens.spotifycodingchallenge.commons.Origin;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public Album create(AlbumByArtistSpotifyDto albumsByArtistSpotifyDto, List<ArtistSpotifyDto> artistSpotifyDtos) {
        List<Artist> artists = artistSpotifyDtos.stream()
                .map(artistRetriever::getOrCreateBySpotify)
                .toList();
        String externalSpotifyUrl = albumsByArtistSpotifyDto.externalUrls().spotify();
        String imageUrl = albumsByArtistSpotifyDto.images().stream()
                .map(Image::url)
                .findFirst()
                .orElse(null);
        Restriction restriction = albumsByArtistSpotifyDto.restriction();
        RestrictionReason restrictionReason = restriction != null ? restriction.reason() : null;

        List<CopyrightDto> copyrightDtos = getCopyrightDtos(albumsByArtistSpotifyDto);
        return createInternal(externalSpotifyUrl, albumsByArtistSpotifyDto.id(), albumsByArtistSpotifyDto.uri(), Origin.SPOTIFY,
                albumsByArtistSpotifyDto.trackCount(), imageUrl, albumsByArtistSpotifyDto.name(), albumsByArtistSpotifyDto.releaseDate(),
                albumsByArtistSpotifyDto.releaseDatePrecision(), restrictionReason, albumsByArtistSpotifyDto.albumType(),
                artists, copyrightDtos);
    }

    private List<CopyrightDto> getCopyrightDtos(AlbumByArtistSpotifyDto albumsByArtistSpotifyDto) {
        List<CopyrightSpotifyDto> copyrightSpotifyDtos = albumsByArtistSpotifyDto.copyrightSpotifyDtos();
        if (copyrightSpotifyDtos != null) {
            return copyrightSpotifyDtos.stream()
                    .map(copyrightSpotifyDto -> new CopyrightDto(null, copyrightSpotifyDto.text(), copyrightSpotifyDto.type()))
                    .toList();
        }
        return List.of();
    }
}
