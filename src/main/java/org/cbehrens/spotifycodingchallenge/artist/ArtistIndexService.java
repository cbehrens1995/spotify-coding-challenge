package org.cbehrens.spotifycodingchallenge.artist;

import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Component
public class ArtistIndexService {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public ArtistIndexService(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Artist> searchInIndex(String search, Integer maxResult) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        SearchSession searchSession = Search.session(entityManager);

        return getArtistsByWildcardSearch(search, searchSession, maxResult);
    }

    private List<Artist> getArtistsByWildcardSearch(String search, SearchSession searchSession, Integer maxResult) {
        return searchSession.search(Artist.class)
                .where(f -> f.wildcard()
                        .fields(Artist.FieldInfo.ARTIST_NAME)
                        .matching(String.format("*%s*", search)))
                .fetchHits(maxResult);
    }
}
