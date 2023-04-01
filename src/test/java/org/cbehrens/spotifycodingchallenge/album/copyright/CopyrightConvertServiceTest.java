package org.cbehrens.spotifycodingchallenge.album.copyright;

import org.cbehrens.spotifycodingchallenge.commons.AbstractDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CopyrightConvertServiceTest {

    private CopyrightConvertService testee;

    @BeforeEach
    void init() {
        testee = new CopyrightConvertService();
    }

    @Test
    void thatToDtoWorks() {
        //given
        var text = "Copyright to you";
        var copyrightType = CopyrightType.C;
        var id = 187L;
        var copyright = CopyrightBuilder.copyright(id)
                .withText(text)
                .withCopyrightType(copyrightType)
                .build();

        //when
        CopyrightDto result = testee.toDto(copyright);

        //then
        assertThat(result)
                .returns(id, AbstractDto::getId)
                .returns(text, CopyrightDto::getText)
                .returns(copyrightType, CopyrightDto::getCopyrightType);
    }

    @Test
    void thatToDtosWorks() {
        //given
        var id1 = 186L;
        var id2 = 187L;
        var copyright1 = CopyrightBuilder.copyright(id1).build();
        var copyright2 = CopyrightBuilder.copyright(id2).build();

        //when
        List<CopyrightDto> result = testee.toDtos(List.of(copyright1, copyright2));

        //then
        assertThat(result).extracting(
                AbstractDto::getId
        ).containsExactlyInAnyOrder(
                id1, id2
        );
    }
}