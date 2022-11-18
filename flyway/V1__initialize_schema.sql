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

CREATE TABLE dependency_graph (
    id VARCHAR(50) NOT NULL,
    chapter_number INTEGER NOT NULL,
    chapter_name VARCHAR(30) NOT NULL,
    graph_text text NOT NULL ,
    document text NOT NULL,
    verses INTEGER [] NOT NULL,
    PRIMARY KEY (id)
);

CREATE TABLE dependency_graph_verse_tokens_rln (
    graph_id VARCHAR(50) NOT NULL,
    chapter_number INTEGER NOT NULL,
    verse_number INTEGER NOT NULL,
    tokens INTEGER [] NOT NULL,
    PRIMARY KEY (graph_id, chapter_number, verse_number),
    CONSTRAINT fk_dependency_graph FOREIGN KEY (graph_id) REFERENCES dependency_graph(id)
);

CREATE TABLE graph_node (
    graph_id VARCHAR(50) NOT NULL,
    node_id VARCHAR(50) NOT NULL,
    document text NOT NULL,
    PRIMARY KEY (graph_id, node_id),
    CONSTRAINT fk_dependency_graph FOREIGN KEY (graph_id) REFERENCES dependency_graph(id)
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
