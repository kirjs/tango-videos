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
                "MATCH (od:Dancer {id: {oldName}})-[r:DANCES]->(v:Video) " +
                "MERGE (nd:Dancer {id: {newName}}) " +
                "MERGE (nd)-[:DANCES]->(v) " +
                "DELETE od " +
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
        try (Transaction tx = graphDb.beginTx();
             Result videos = graphDb.execute("MATCH (v:Video) RETURN count(v) as videos");
             Result activeDancers = graphDb.execute("MATCH (d:Dancer)-[:DANCES]->(v:Video) RETURN count(DISTINCT d) as dancers");
             Result allDancers = graphDb.execute("MATCH (d:Dancer) RETURN count(DISTINCT d) as dancers");
             Result allSongs = graphDb.execute("MATCH (s:Songs) RETURN count(DISTINCT s) as songs")
        ) {
            stats = ImmutableList.of(
                    new KeyValue("videos", videos.hasNext() ? videos.columnAs("videos").next().toString() : "0"),
                    new KeyValue("activeDancers", activeDancers.hasNext() ? activeDancers.columnAs("dancers").next().toString() : "0"),
                    new KeyValue("allDancers", allDancers.hasNext() ? allDancers.columnAs("dancers").next().toString() : "0"),
                    new KeyValue("songs", allSongs.hasNext() ? allSongs.columnAs("songs").next().toString() : "0")
            );
            tx.success();
        }
        return stats;
    }
}
