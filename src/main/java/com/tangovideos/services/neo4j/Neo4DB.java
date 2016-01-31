package com.tangovideos.services.neo4j;

import com.tangovideos.services.Interfaces.ServiceFactory;
import com.tangovideos.services.Interfaces.UserService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4DB {
    private static final String DB_PATH = "neo4jDb6";
    public final GraphDatabaseService graphDb;

    public Neo4DB() {

        final File storeDir = new File(DB_PATH);
        if(storeDir.exists()){
            storeDir.delete();
        }
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(storeDir);
    }
}
