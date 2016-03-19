package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;

public class Neo4DB {
    private static final String DB_PATH = "neo4jDb12";
    public final GraphDatabaseService graphDb;

    public Neo4DB() {
        final File storeDir = new File("/usr/local/tomcat/bin/neo4jDb12");
        graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(storeDir);
    }

    public static void main(String[] args){
        final GraphDatabaseService graphDb = new Neo4DB().graphDb;
        final String query = "MATCH (d:Dancer {id: 'Carlos Espinoza'})--(n) " +
                "WHERE NOT n:Video " +
                "RETURN d, labels(n)    ";
        final ImmutableMap<String, Object> params = ImmutableMap.of();

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            System.out.println(result.resultAsString());
            tx.success();
        }
    }
}
