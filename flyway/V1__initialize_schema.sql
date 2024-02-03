-- CREATE SCHEMA morphological_analysis;

CREATE TABLE chapter
(
    chapter_number INTEGER     NOT NULL,
    chapter_name   VARCHAR(50) NOT NULL,
    verse_count    INTEGER     NOT NULL,
    PRIMARY KEY (chapter_number)
);

CREATE TABLE verse
(
    id             bigint  NOT NULL,
    verse_number   INTEGER NOT NULL,
    chapter_number INTEGER NOT NULL REFERENCES chapter (chapter_number),
    token_count    INTEGER NOT NULL,
    verse_text     text    NOT NULL,
    translation    text,
    PRIMARY KEY (id)
);

CREATE INDEX IF NOT EXISTS verse_chapter_number_idx ON verse(chapter_number);

CREATE TABLE token
(
    id             bigint  NOT NULL,
    token_number   INTEGER NOT NULL,
    verse_number   INTEGER NOT NULL,
    chapter_number INTEGER NOT NULL,
    verse_id       bigint  NOT NULL REFERENCES verse (id),
    token_text     text    NOT NULL,
    derived_text   text    NOT NULL,
    hidden         boolean NOT NULL,
    translation    text,
    PRIMARY KEY (id)
);

CREATE TABLE location
(
    id              bigint      NOT NULL,
    location_number INTEGER     NOT NULL,
    token_number    INTEGER     NOT NULL,
    verse_number    INTEGER     NOT NULL,
    chapter_number  INTEGER     NOT NULL,
    token_id        bigint      NOT NULL REFERENCES token (id) ON DELETE CASCADE,
    verse_id        bigint      NOT NULL REFERENCES verse (id) ON DELETE CASCADE,
    hidden          BOOLEAN     NOT NULL,
    start_index     INTEGER     NOT NULL,
    end_index       INTEGER     NOT NULL,
    derived_text    text        NOT NULL,
    location_text   text        NOT NULL,
    alternate_text  text        NOT NULL,
    word_type       VARCHAR(20) NOT NULL,
    properties      text        NOT NULL,
    translation     text,
    named_tag       VARCHAR(20),
    PRIMARY KEY (id)
);

GRANT
SELECT,
INSERT
,
DELETE,
UPDATE, REFERENCES, TRIGGER
ON ALL TABLES IN SCHEMA ${schema} TO morphological_analysis;

GRANT
USAGE,
SELECT,
UPDATE
ON ALL SEQUENCES IN SCHEMA ${schema} TO morphological_analysis;

GRANT
USAGE
ON
SCHEMA
${schema} TO morphological_analysis;
