-- CREATE SCHEMA morphological_analysis;

CREATE TABLE location (
    id VARCHAR(50) NOT NULL,
    token_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE token (
    id VARCHAR(50) NOT NULL,
    verse_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE verse (
    id VARCHAR(50) NOT NULL,
    chapter_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE chapter (
    id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (id)
);

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES, TRIGGER
      ON ALL TABLES IN SCHEMA ${schema}
          TO morphological_analysis;

GRANT USAGE, SELECT, UPDATE
             ON ALL SEQUENCES IN SCHEMA ${schema}
                 TO morphological_analysis;

GRANT USAGE
ON SCHEMA ${schema}
    TO morphological_analysis;
