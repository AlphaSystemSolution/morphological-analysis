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

GRANT SELECT, INSERT, DELETE, UPDATE, REFERENCES, TRIGGER
      ON ALL TABLES IN SCHEMA ${schema}
          TO morphological_analysis;

GRANT USAGE, SELECT, UPDATE
             ON ALL SEQUENCES IN SCHEMA ${schema}
                 TO morphological_analysis;

GRANT USAGE
ON SCHEMA ${schema}
    TO morphological_analysis;
