package org.cbehrens.spotifycodingchallenge.album;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightType;
import org.cbehrens.spotifycodingchallenge.album.spotify.AlbumSpotifyDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.CopyrightSpotifyDto;
import org.cbehrens.spotifycodingchallenge.album.spotify.Restriction;
import org.cbehrens.spotifycodingchallenge.artist.*;
import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;
import org.cbehrens.spotifycodingchallenge.commons.SpotifyEntityUpdater;
import org.cbehrens.spotifycodingchallenge.commons.spotify.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class AlbumUpdater extends SpotifyEntityUpdater<Album> {

    private final SpotifyDtoValidator spotifyDtoValidator;
    private final ArtistRetriever artistRetriever;
    private final ArtistRepository artistRepository;

    @Autowired
    public AlbumUpdater(SpotifyDtoValidator spotifyDtoValidator, ArtistRetriever artistRetriever, ArtistRepository artistRepository) {
        this.spotifyDtoValidator = spotifyDtoValidator;
        this.artistRetriever = artistRetriever;
        this.artistRepository = artistRepository;
    }

    public Album update(Album album, AlbumDto albumDto) {
        spotifyDtoValidator.assertDtoHasNoSpotifyInformation(albumDto);

        boolean isBasicFieldUpdated = updateBasicFieldIfNecessary(album, albumDto.getTrackCount(), albumDto.getImageUrl(),
                albumDto.getName(), albumDto.getReleaseDate(), albumDto.getReleaseDatePrecision(),
                albumDto.getRestrictionReason(), albumDto.getAlbumType());

        boolean areArtistsUpdated = updateArtistsByArtistDtos(album, albumDto.getArtistDtos());
        boolean areCopyrightsUpdated = updateCopyRightsByCopyrightDtos(album, albumDto.getCopyrightDtos());

        if (isBasicFieldUpdated || areArtistsUpdated || areCopyrightsUpdated) {
            album.setManuallyAdjusted();
        }

        return album;
    }

    private boolean updateBasicFieldIfNecessary(Album album,
                                                Integer trackCount,
                                                String imageUrl,
                                                String name,
                                                String releaseDate,
                                                ReleaseDatePrecision releaseDatePrecision,
                                                RestrictionReason restrictionReason,
                                                AlbumType albumType) {

        boolean isTrackCountUpdated = updateIfChanged(album, Album::getTrackCount, Album::setTrackCount, trackCount);
        boolean isImageUrlUpdated = updateIfChanged(album, Album::getImageUrl, Album::setImageUrl, imageUrl);
        boolean isNameUpdated = updateIfChanged(album, Album::getName, Album::setName, name);
        boolean isReleaseDateUpdated = updateIfChanged(album, Album::getReleaseDate, Album::setReleaseDate, releaseDate);
        boolean isReleaseDatePrecisionUpdated = updateIfChanged(album, Album::getReleaseDatePrecision, Album::setReleaseDatePrecision, releaseDatePrecision);
        boolean isRestrictionReasonUpdated = updateIfChanged(album, Album::getRestrictionReason, Album::setRestrictionReason, restrictionReason);
        boolean isAlbumTypeUpdated = updateIfChanged(album, Album::getAlbumType, Album::setAlbumType, albumType);

        return isTrackCountUpdated || isImageUrlUpdated || isNameUpdated || isReleaseDateUpdated || isReleaseDatePrecisionUpdated
                || isRestrictionReasonUpdated || isAlbumTypeUpdated;
    }

    private boolean updateCopyRightsByCopyrightDtos(Album album, List<CopyrightDto> copyrightDtos) {
        List<Pair<CopyrightType, String>> newCopyrightPairs = copyrightDtos.stream()
                .map(copyright -> Pair.of(copyright.getCopyrightType(), copyright.getText()))
                .toList();

        return updateCopyrightsIfNecessary(album, newCopyrightPairs);
    }

    private boolean updateCopyRightsByCopyrightSpotifyDtos(Album album, List<CopyrightSpotifyDto> copyrightSpotifyDtos) {
        List<Pair<CopyrightType, String>> newCopyrightPairs = copyrightSpotifyDtos.stream()
                .map(copyrightSpotifyDto -> Pair.of(copyrightSpotifyDto.type(), copyrightSpotifyDto.text()))
                .toList();

        return updateCopyrightsIfNecessary(album, newCopyrightPairs);
    }

    /**
     * Copyrights should only be updated if a copyright has been added or removed
     */
    private boolean updateCopyrightsIfNecessary(Album album, List<Pair<CopyrightType, String>> newCopyrightPairs) {
        List<Pair<CopyrightType, String>> currentCopyrightPairs = album.getCopyrights().stream()
                .map(copyright -> Pair.of(copyright.getCopyrightType(), copyright.getText()))
                .toList();

        if (CollectionUtils.containsAll(currentCopyrightPairs, newCopyrightPairs) &&
                CollectionUtils.containsAll(newCopyrightPairs, currentCopyrightPairs)) {
            return false;
        }

        album.clearCopyrights();
        newCopyrightPairs.stream()
                .map(copyrightPair -> new Copyright(copyrightPair.getValue(), copyrightPair.getKey(), album))
                .forEach(album::addCopyright);

        return true;
    }

    /**
     * The artists should only be updated if there is a changed
     * this happens if either a unknown artists is needed
     * or an existing artist is added to or removed from the current artist list
     */
    private boolean updateArtistsByArtistDtos(Album album, List<ArtistDto> artistDtos) {
        boolean hasNewArtist = artistDtos.stream()
                .map(AbstractDto::getId)
                .anyMatch(Objects::isNull);
        if (hasNewArtist) {
            List<Artist> artists = artistDtos.stream()
                    .map(artistRetriever::getOrCreate)
                    .toList();

            album.clearArtist();
            artists.forEach(album::addArtist);
            return true;
        }

        List<Long> artistIds = artistDtos.stream()
                .map(AbstractDto::getId)
                .toList();
        List<Artist> newArtists = artistRepository.findAllByIdOrThrow(artistIds);
        List<Artist> currentArtists = album.getArtists();

        if (CollectionUtils.containsAll(currentArtists, newArtists) &&
                CollectionUtils.containsAll(newArtists, currentArtists)) {
            return false;
        }

        album.clearArtist();
        newArtists.forEach(album::addArtist);

        return true;
    }

    public void update(Album album, AlbumSpotifyDto albumSpotifyDto) {
        String imageUrl = albumSpotifyDto.images().stream()
                .map(Image::url)
                .findFirst().orElse(null);
        Restriction restriction = albumSpotifyDto.restriction();
        RestrictionReason restrictionReason = restriction != null ? restriction.reason() : null;
        updateBasicFieldIfNecessary(album, albumSpotifyDto.trackCount(), imageUrl, albumSpotifyDto.name(),
                albumSpotifyDto.releaseDate(), albumSpotifyDto.releaseDatePrecision(), restrictionReason,
                albumSpotifyDto.albumType());

        updateCopyRightsByCopyrightSpotifyDtos(album, albumSpotifyDto.copyrightSpotifyDtos());

        album.clearArtist();
        albumSpotifyDto.artistSpotifyDtos().stream()
                .map(artistRetriever::getOrCreateBySpotify)
                .forEach(album::addArtist);
    }
}
