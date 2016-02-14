package com.tangovideos.services.neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4DB {
    private static final String DB_PATH = "neo4jDb12";
    public final GraphDatabaseService graphDb;

    public Neo4DB() {
        final File storeDir = new File(DB_PATH);
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(storeDir);
    }
}
