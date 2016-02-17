package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.KeyValue;
import com.tangovideos.services.Interfaces.AdminToolsService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.List;

import static com.google.common.collect.ImmutableMap.of;

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
        final ImmutableMap<String, Object> params = of("oldName", oldName, "newName", newName);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {

            tx.success();
        }
    }

    @Override
    public List<KeyValue> stats() {
        List<KeyValue> stats;
        final String query = "MATCH (v:Video)" +
                "" +
                " RETURN count(v) as videos";
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
            stats = ImmutableList.of(new KeyValue("videos", result.columnAs("videos").next().toString()));
            tx.success();
        }
        return stats;
    }
}
