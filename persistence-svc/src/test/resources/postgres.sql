CREATE SCHEMA morphological_analysis;
SET search_path TO "morphological_analysis";

CREATE TABLE word_properties (
    id VARCHAR(50) NOT NULL,
    location_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE location (
    id VARCHAR(50) NOT NULL,
    token_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (id)
);
