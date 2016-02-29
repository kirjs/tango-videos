package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tangovideos.models.KeyValue;
import com.tangovideos.models.VideoAndDancer;
import com.tangovideos.services.Interfaces.AdminToolsService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.List;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;

public class Neo4jAdminToolsService implements AdminToolsService {
    final private GraphDatabaseService graphDb;

    public Neo4jAdminToolsService(GraphDatabaseService graphDb) {

        this.graphDb = graphDb;
    }

    @Override
    public long renameDancer(String oldName, String newName) {
        final String query = "" +
                "MATCH (od:Dancer {id: {oldName}})-[r:DANCES]->(v:Video) " +
                "MERGE (nd:Dancer {id: {newName}}) " +
                "MERGE (nd)-[:DANCES]->(v) " +
                "DELETE od " +
                "DELETE r " +
                "RETURN r";
        final ImmutableMap<String, Object> params = of("oldName", oldName, "newName", newName);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            tx.success();
            return IteratorUtil.count(result);
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

    @Override
    public List<VideoAndDancer> getVideosAndDancers() {
        final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) return v, collect(d.id) as dancers";
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
            final List<VideoAndDancer> videoAndDancerStream = IteratorUtil.asList(result).stream().map(r -> {
                final VideoAndDancer videoAndDancer = new VideoAndDancer();
                final Node v = (Node) r.get("v");

                videoAndDancer.setDancers(Sets.newHashSet((Iterable<String>) r.get("dancers")));
                videoAndDancer.setVideoId(v.getProperty("id").toString());
                videoAndDancer.setTitle(v.getProperty("title").toString());
                videoAndDancer.setDescription(v.getProperty("description").toString());
                return videoAndDancer;
            }).collect(Collectors.toList());
            tx.success();
            return videoAndDancerStream;
        }
    }
}
