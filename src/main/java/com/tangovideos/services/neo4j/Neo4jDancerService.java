package com.tangovideos.services.neo4j;

import com.google.api.client.util.Sets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.models.Dancer;
import com.tangovideos.models.VideoResponse;
import com.tangovideos.services.Interfaces.DancerService;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class Neo4jDancerService implements DancerService {
    private final GraphDatabaseService graphDb;
    private Label label = Labels.DANCER.label;
    public enum SortBy {
        VIDEO_COUNT
    }
    public Neo4jDancerService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;


    }

    public Dancer mapNode(Node node) {
        final Dancer dancer = new Dancer();
        dancer.setName(node.getProperty("id").toString());
        return dancer;
    }


    @Override
    public List<Dancer> list() {
        List<Dancer> dancers = Lists.newArrayList();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "RETURN DISTINCT d as dancer";
            final ResourceIterator<Node> result = this.graphDb.execute(query).columnAs("dancer");
            dancers = IteratorUtil.asList(result).stream().map(this::mapNode).collect(Collectors.toList());
            tx.success();
        }

        return dancers;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Dancer> list(int skip, int limit, SortBy sortBy) {
        List<Dancer> dancers = Lists.newArrayList();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "RETURN DISTINCT d as dancer, collect(v.id) as videos, count(v.id) as count " +
                    "ORDER BY count DESC";
            ImmutableMap.of("skip", skip, "limit", limit);
            final Result result = this.graphDb.execute(query);


            while(result.hasNext()){
                final Map<String, Object> next = result.next();
                final Dancer dancer = this.mapNode((Node) next.get("dancer"));
                final List<String> videos = IteratorUtil.asList((Iterable<String>) next.get("videos"));
                final List<VideoResponse> collect = videos.stream().map(v -> new VideoResponse()).collect(Collectors.toList());
                dancer.setVideos(collect);
                dancers.add(dancer);
            }

            tx.success();
        }

        return dancers;
    }


    @Override
    public void addToVideo(Node dancer, Node video) {
        String query = "MATCH (d:Dancer {id: {dancerId}}) " +
                "MATCH (v:Video {id: {videoId}})" +
                "MERGE (d)-[:DANCES]->(v)" +
                "RETURN d";
        final ImmutableMap<String, Object> params = ImmutableMap.of("videoId", video.getProperty("id"), "dancerId", dancer.getProperty("id"));

        try (Transaction tx = this.graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            tx.success();
        }
    }

    @Override
    public void removeFromVideo(Node dancer, Node video) {
        try(Transaction tx = graphDb.beginTx()) {
            for (Relationship relationship : video.getRelationships(Relationships.DANCES, Direction.INCOMING)) {
                if (relationship.getOtherNode(video).equals(dancer)) {
                    relationship.delete();
                }
            }
            tx.success();
        }

    }

    @Override
    public Node insertOrGetNode(String dancerId) {
        Node dancer;

        try (Transaction tx = this.graphDb.beginTx()) {
            final ResourceIterator<Node> nodes = this.graphDb.findNodes(Labels.DANCER.label, "id", dancerId);
            if (nodes.hasNext()) {
                dancer = nodes.next();
            } else {
                dancer = this.graphDb.createNode(Labels.DANCER.label);
                dancer.setProperty("id", dancerId);
            }
            nodes.close();
            tx.success();
        }
        return dancer;
    }

    @Override
    public Set<String> getForVideo(String videoId) {
        HashSet<String> result = Sets.newHashSet();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "WHERE v.id = {id} " +
                    "RETURN d.id";

            final Result results = this.graphDb.execute(query, ImmutableMap.of("id", videoId));
            while (results.hasNext()) {
                result.add(results.next().get("d.id").toString());
            }

            results.close();
            tx.success();
        }

        return result;
    }

    @Override
    public Dancer get(String id) {
        Dancer dancer;
        try (final Transaction tx = this.graphDb.beginTx()) {
            dancer = mapNode(graphDb.findNode(label, "id", id));
            tx.success();
        }
        return dancer;
    }

}
