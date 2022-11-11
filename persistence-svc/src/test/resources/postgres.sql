CREATE SCHEMA morphological_analysis;
SET search_path TO "morphological_analysis";

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

CREATE TABLE dependency_graph (
    id VARCHAR(50) NOT NULL,
    chapter_number INTEGER NOT NULL,
    verse_number INTEGER NOT NULL,
    start_token_number INTEGER NOT NULL,
    end_token_number INTEGER NOT NULL,
    graph_text text NOT NULL ,
    document text NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE graph_node (
    graph_id VARCHAR(50) NOT NULL,
    node_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (graph_id, node_id),
    CONSTRAINT fk_dependency_graph FOREIGN KEY (graph_id) REFERENCES dependency_graph(id)
);
