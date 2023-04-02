package org.cbehrens.spotifycodingchallenge.commons;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import com.github.tomakehurst.wiremock.matching.AnythingPattern;
import com.github.tomakehurst.wiremock.matching.EqualToPattern;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ExtendWith(WireMockExtension.class)
public abstract class AbstractIntegrationTest {

    @Container
    public static PostgreSQLContainer postgreSQLContainer = PostgresContainer.getInstance();

    @RegisterExtension
    static WireMockExtension wireMockServer = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().dynamicPort())
            .build();


    @DynamicPropertySource
    static void registerPostgresTestContainer(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);

        registry.add("spring.security.oauth2.client.provider.spotify-developer.token-uri", () -> wireMockServer.baseUrl() + "/token");
        registry.add("artist.client.url", () -> wireMockServer.baseUrl() + "/artist");
        registry.add("album.client.url", () -> wireMockServer.baseUrl() + "/album");

        registry.add("artist.spotify.ids", () -> "abcdefg,hijklmno");
    }

    @BeforeAll
    static void initWireMocks() {
        wireMockServer.stubFor(
                WireMock.post("/token")
                        .withRequestBody(new AnythingPattern())
                        .willReturn(WireMock.okForContentType(MediaType.APPLICATION_JSON_VALUE,
                                """
                                        {
                                          "access_token": "AccessTokenUUID",
                                          "token_type": "Bearer",
                                          "expires_in": 3600
                                        }
                                        """))
        );

        wireMockServer.stubFor(
                WireMock.get(WireMock.urlPathMatching("/artist"))
                        .withHeader("Authorization", new EqualToPattern("Bearer AccessTokenUUID"))
                        .willReturn(WireMock.okForContentType(MediaType.APPLICATION_JSON_VALUE,
                                """
                                        {
                                          "artists": [
                                            {
                                              "external_urls": {
                                                "spotify": "https://open.spotify.com/artist/2CIMQHirSU0MQqyYHq0eOx"
                                              },
                                              "followers": {
                                                "href": null,
                                                "total": 2790841
                                              },
                                              "genres": [
                                                "canadian electronic",
                                                "edm",
                                                "electro house",
                                                "pop dance",
                                                "progressive house"
                                              ],
                                              "href": "https://api.spotify.com/v1/artists/2CIMQHirSU0MQqyYHq0eOx",
                                              "id": "2CIMQHirSU0MQqyYHq0eOx",
                                              "images": [
                                                {
                                                  "height": 640,
                                                  "url": "https://i.scdn.co/image/ab6761610000e5ebc5ceb05f152103b2b70d3b07",
                                                  "width": 640
                                                },
                                                {
                                                  "height": 320,
                                                  "url": "https://i.scdn.co/image/ab67616100005174c5ceb05f152103b2b70d3b07",
                                                  "width": 320
                                                },
                                                {
                                                  "height": 160,
                                                  "url": "https://i.scdn.co/image/ab6761610000f178c5ceb05f152103b2b70d3b07",
                                                  "width": 160
                                                }
                                              ],
                                              "name": "deadmau5",
                                              "popularity": 70,
                                              "type": "artist",
                                              "uri": "spotify:artist:2CIMQHirSU0MQqyYHq0eOx"
                                            }
                                          ]
                                        }"""))
        );

        wireMockServer.stubFor(
                WireMock.get(WireMock.urlPathMatching("/artist/2CIMQHirSU0MQqyYHq0eOx/albums"))
                        .withHeader("Authorization", new EqualToPattern("Bearer AccessTokenUUID"))
                        .willReturn(WireMock.okForContentType(MediaType.APPLICATION_JSON_VALUE,
                                """
                                        {
                                          "href": "https://api.spotify.com/v1/artists/2CIMQHirSU0MQqyYHq0eOx/albums?offset=0&limit=1&include_groups=single&market=DE&locale=de,en-US;q=0.7,en;q=0.3",
                                          "items": [
                                            {
                                              "album_group": "single",
                                              "album_type": "single",
                                              "artists": [
                                                {
                                                  "external_urls": {
                                                    "spotify": "https://open.spotify.com/artist/2CIMQHirSU0MQqyYHq0eOx"
                                                  },
                                                  "href": "https://api.spotify.com/v1/artists/2CIMQHirSU0MQqyYHq0eOx",
                                                  "id": "2CIMQHirSU0MQqyYHq0eOx",
                                                  "name": "deadmau5",
                                                  "type": "artist",
                                                  "uri": "spotify:artist:2CIMQHirSU0MQqyYHq0eOx"
                                                }
                                              ],
                                              "external_urls": {
                                                "spotify": "https://open.spotify.com/album/4D3QP0SVFo1iRWWACAhmQi"
                                              },
                                              "href": "https://api.spotify.com/v1/albums/4D3QP0SVFo1iRWWACAhmQi",
                                              "id": "4D3QP0SVFo1iRWWACAhmQi",
                                              "images": [
                                                {
                                                  "height": 640,
                                                  "url": "https://i.scdn.co/image/ab67616d0000b2733a7cf0d02eed9a6298bb2808",
                                                  "width": 640
                                                },
                                                {
                                                  "height": 300,
                                                  "url": "https://i.scdn.co/image/ab67616d00001e023a7cf0d02eed9a6298bb2808",
                                                  "width": 300
                                                },
                                                {
                                                  "height": 64,
                                                  "url": "https://i.scdn.co/image/ab67616d000048513a7cf0d02eed9a6298bb2808",
                                                  "width": 64
                                                }
                                              ],
                                              "name": "Sacrifice",
                                              "release_date": "2023-02-17",
                                              "release_date_precision": "day",
                                              "total_tracks": 1,
                                              "type": "album",
                                              "uri": "spotify:album:4D3QP0SVFo1iRWWACAhmQi"
                                            }
                                          ],
                                          "limit": 1,
                                          "next": "https://api.spotify.com/v1/artists/2CIMQHirSU0MQqyYHq0eOx/albums?offset=1&limit=1&include_groups=single&market=DE&locale=de,en-US;q=0.7,en;q=0.3",
                                          "offset": 0,
                                          "previous": null,
                                          "total": 143
                                        }"""))
        );
    }

}
