package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.services.Interfaces.AdminToolsService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

public class Neo4jAdminToolsService implements AdminToolsService {
    final private GraphDatabaseService graphDb;

    public Neo4jAdminToolsService(GraphDatabaseService graphDb) {

        this.graphDb = graphDb;
    }

    @Override
    public void renameDancer(String oldName, String newName) {
        final String query = "" +
                "MATCH (o:Dancer {id: {oldName}})-[r:DANCES]->(v:Video) " +
                "MERGE (n:Dancer {id: {newName}})-[:DANCES]->(v) " +
                "DELETE r " +
                "RETURN v";
        final ImmutableMap<String, Object> params = ImmutableMap.of("oldName", oldName, "newName", newName);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {

            tx.success();
        }
    }
}
