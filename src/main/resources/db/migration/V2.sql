CREATE TABLE IF NOT EXISTS album
(
    id                     BIGINT  NOT NULL PRIMARY KEY,
    external_spotify_url   TEXT,
    spotify_id             TEXT,
    uri                    TEXT,
    origin                 TEXT    NOT NULL,
    manually_adjusted      BOOLEAN NOT NULL DEFAULT false,

    track_count            INT,

    image_url              TEXT,
    album_name             TEXT,
    release_date           TEXT,
    release_date_precision TEXT,
    restriction_reason     TEXT,
    album_type             TEXT    NOT NULL
);

CREATE TABLE IF NOT EXISTS copyright
(
    id             BIGINT                       NOT NULL PRIMARY KEY,
    fk_album       BIGINT REFERENCES album (id) NOT NULL,
    copyright_text TEXT,
    copyright_type TEXT                         NOT NULL
);

CREATE TABLE IF NOT EXISTS album_to_artist
(
    fk_album  BIGINT REFERENCES album (id)  NOT NULL,
    fk_artist BIGINT REFERENCES artist (id) NOT NULL,

    PRIMARY KEY (fk_album, fk_artist)

);