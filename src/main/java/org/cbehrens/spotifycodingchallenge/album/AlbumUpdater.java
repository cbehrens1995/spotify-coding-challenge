package org.cbehrens.spotifycodingchallenge.album;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cbehrens.spotifycodingchallenge.album.copyright.Copyright;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightDto;
import org.cbehrens.spotifycodingchallenge.album.copyright.CopyrightType;
import org.cbehrens.spotifycodingchallenge.artist.*;
import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;
import org.cbehrens.spotifycodingchallenge.commons.SpotifyEntityUpdater;
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

        boolean isTrackCountUpdated = updateIfChanged(album, Album::getTrackCount, Album::setTrackCount, albumDto.getTrackCount());
        boolean isImageUrlUpdated = updateIfChanged(album, Album::getImageUrl, Album::setImageUrl, albumDto.getImageUrl());
        boolean isNameUpdated = updateIfChanged(album, Album::getName, Album::setName, albumDto.getName());
        boolean isReleaseDateUpdated = updateIfChanged(album, Album::getReleaseDate, Album::setReleaseDate, albumDto.getReleaseDate());
        boolean isReleaseDatePrecisionUpdated = updateIfChanged(album, Album::getReleaseDatePrecision, Album::setReleaseDatePrecision, albumDto.getReleaseDatePrecision());
        boolean isRestrictionReasonUpdated = updateIfChanged(album, Album::getRestrictionReason, Album::setRestrictionReason, albumDto.getRestrictionReason());
        boolean isAlbumTypeUpdated = updateIfChanged(album, Album::getAlbumType, Album::setAlbumType, albumDto.getAlbumType());

        boolean areArtistsUpdated = updateArtistsIfNecessary(album, albumDto.getArtistDtos());
        boolean areCopyrightsUpdated = updateCopyRightsIfNecessary(album, albumDto.getCopyrightDtos());

        if (isTrackCountUpdated || isImageUrlUpdated || isNameUpdated || isReleaseDateUpdated || isReleaseDatePrecisionUpdated
                || isRestrictionReasonUpdated || isAlbumTypeUpdated || areArtistsUpdated || areCopyrightsUpdated) {
            album.setManuallyAdjusted();
        }

        return album;
    }

    /**
     * Copyrights should only be updated if a copyright has been added or removed
     */
    private boolean updateCopyRightsIfNecessary(Album album, List<CopyrightDto> copyrightDtos) {
        List<Pair<CopyrightType, String>> currentCopyrightPairs = album.getCopyrights().stream()
                .map(copyright -> Pair.of(copyright.getCopyrightType(), copyright.getText()))
                .toList();

        List<Pair<CopyrightType, String>> newCopyrightPairs = copyrightDtos.stream()
                .map(copyright -> Pair.of(copyright.getCopyrightType(), copyright.getText()))
                .toList();

        if (CollectionUtils.containsAll(currentCopyrightPairs, newCopyrightPairs) &&
                CollectionUtils.containsAll(newCopyrightPairs, currentCopyrightPairs)) {
            return false;
        }

        album.clearCopyrights();
        copyrightDtos.stream()
                .map(copyrightDto -> new Copyright(copyrightDto.getText(), copyrightDto.getCopyrightType(), album))
                .forEach(album::addCopyright);

        return true;
    }

    /**
     * The artists should only be updated if there is a changed
     * this happens if either a unknown artists is needed
     * or an existing artist is added to or removed from the current artist list
     */
    private boolean updateArtistsIfNecessary(Album album, List<ArtistDto> artistDtos) {
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
}
