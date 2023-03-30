CREATE SEQUENCE hibernate_sequence START 1 INCREMENT 1;

CREATE TABLE IF NOT EXISTS artist
(
    id                   BIGINT  NOT NULL PRIMARY KEY,
    external_spotify_url TEXT,
    spotify_id           TEXT,
    uri                  TEXT,
    origin               TEXT    NOT NULL,
    manually_adjusted    BOOLEAN NOT NULL DEFAULT false,

    followers_count      INT,
    image_url            TEXT,
    artist_name          TEXT    NOT NULL,
    popularity           INT
)