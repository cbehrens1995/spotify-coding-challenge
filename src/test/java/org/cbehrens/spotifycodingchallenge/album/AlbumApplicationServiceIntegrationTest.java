package org.cbehrens.spotifycodingchallenge.album;

import org.cbehrens.spotifycodingchallenge.artist.ArtistApplicationService;
import org.cbehrens.spotifycodingchallenge.artist.ArtistDtoBuilder;
import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;
import org.cbehrens.spotifycodingchallenge.commons.AbstractIntegrationTest;
import org.cbehrens.spotifycodingchallenge.commons.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class AlbumApplicationServiceIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private AlbumApplicationService testee;
    @Autowired
    private ArtistApplicationService artistApplicationService;

    @Test
    void thatNotExistingIdsThrowExceptions_Get_Delete() {
        //when /then
        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> testee.getById(1L));

        assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> testee.deleteById(1L));
    }

    /**
     * In this test we want to create an album with a new artist
     * Then we are deleting the artist but ensure that the album won't
     * In the end we are adding an existing artist to the album
     */
    @Test
    void thatCRUDWorks() {
        // CREATE part
        var artistName1 = "Fresh D";
        var artistDto1 = ArtistDtoBuilder.artistDto(null)
                .withName(artistName1)
                .build();

        var albumName = "DumbleCore";
        var albumDto = AlbumDtoBuilder.albumDto(null)
                .withName(albumName)
                .withAlbumType(AlbumType.ALBUM)
                .addArtistDto(artistDto1)
                .build();

        AlbumDto resultDto = testee.create(albumDto);
        var albumId = resultDto.getId();
        assertThat(albumId).isNotNull();
        assertThat(resultDto.getName()).isEqualTo(albumName);
        assertThat(resultDto.getArtistDtos()).hasSize(1);

        // READ part - check if mapping between album and artist is bidirectional
        var resultedArtistDto = resultDto.getArtistDtos().get(0);
        var artistId = resultedArtistDto.getId();
        assertThat(artistId).isNotNull();
        assertThat(resultedArtistDto.getName()).isEqualTo(artistName1);

        var albumDtosByArtist = testee.getByArtist(artistId);
        assertThat(albumDtosByArtist).extracting(
                AbstractDto::getId
        ).containsExactly(albumId);

        // DELETE the artist
        assertThatNoException().isThrownBy(() -> artistApplicationService.deleteById(artistId));
        var albumDtoWithoutArtist = testee.getById(albumId);
        assertThat(albumDtoWithoutArtist.getArtistDtos()).isEmpty();

        //Update the album
        var artistName2 = "Fresh D No2";
        var artistId2 = createArtist(artistName2);
        var existingArtistDto = ArtistDtoBuilder.artistDto(artistId2)
                .withName(artistName2)
                .build();

        var updatedAlbumDto = AlbumDtoBuilder.albumDto(resultDto.getId())
                .withName(albumName)
                .withAlbumType(AlbumType.ALBUM)
                .addArtistDto(existingArtistDto)
                .build();
        var resultedUpdatedAlbumDto = testee.update(updatedAlbumDto);
        assertThat(resultedUpdatedAlbumDto.getArtistDtos()).extracting(
                AbstractDto::getId
        ).containsExactly(artistId2);
    }

    private Long createArtist(String name) {
        var artistDto = ArtistDtoBuilder.artistDto(null)
                .withName(name)
                .build();
        var resultedArtistDto = artistApplicationService.create(artistDto);
        return resultedArtistDto.getId();
    }
}