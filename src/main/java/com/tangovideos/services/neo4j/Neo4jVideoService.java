package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Video;
import com.tangovideos.models.VideoResponse;
import com.tangovideos.services.Interfaces.VideoService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;

    public Neo4jVideoService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public boolean exists(String videoId) {
        try (Transaction tx = graphDb.beginTx()) {
            final boolean result = this.graphDb.findNodes(Labels.VIDEO.label, "id", videoId).hasNext();
            tx.success();
            return result;
        }
    }

    @Override
    public Node addVideo(@NotNull Video video) {
        final Node node;
        try (Transaction tx = graphDb.beginTx()) {
            if (exists(video.getId())) {
                throw new RuntimeException("VideoExists");
            }

            node = graphDb.createNode(Labels.VIDEO.label);
            node.setProperty("title", video.getTitle());
            node.setProperty("description", video.getDescription());
            node.setProperty("publishedAt", video.getPublishedAt());
            node.setProperty("recordedAt", video.getPublishedAt());
            node.setProperty("addedAt", Instant.now().getEpochSecond());
            node.setProperty("id", video.getId());
            tx.success();
        }
        return node;
    }

    @Override
    public Node get(String videoId) {
        Node video;
        try (Transaction tx = this.graphDb.beginTx()) {
            video = this.graphDb.findNode(Labels.VIDEO.label, "id", videoId);
            tx.success();
        }
        return video;
    }

    public static VideoResponse videoResponseFromNode(Node node) {
        VideoResponse video = new VideoResponse();
        video.setId(node.getProperty("id").toString());
        video.setPublishedAt(node.getProperty("publishedAt").toString());
        if(node.hasProperty("recordedAt")){
            video.setRecordedAt(node.getProperty("recordedAt").toString());
        }
        video.setTitle(node.getProperty("title").toString());
        video.setAddedAt(node.getProperty("addedAt").toString());
        video.setComplete(node.hasProperty("complete") && node.getProperty("complete").toString().equals("true"));
        return video;
    }

    @Override
    public List<VideoResponse> list() {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs " +
                        "ORDER BY v.addedAt DESC";

        return getMultipleVideos(query, ImmutableMap.of());
    }

    @Override
    public List<VideoResponse> list(int skip, int limit) {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs " +
                        "ORDER BY v.addedAt DESC " +
                        "SKIP {skip} " +
                        "LIMIT {limit}";

        return getMultipleVideos(query, ImmutableMap.of("skip", skip, "limit", limit));
    }

    @Override
    public boolean hide(String id) {
        final String query = "MATCH (v:Video {id: {id}}) " +
                "SET v:VideoRemoved " +
                "REMOVE v:Video " +
                "RETURN v ";

        try (Transaction tx = graphDb.beginTx()) {
            graphDb.execute(query, ImmutableMap.of("id", id));
            tx.success();
            return true;
        }
    }

    @Override
    public List<VideoResponse> listByDancer(String dancerId) {
        final String query =
                "MATCH (v:Video) " +
                        "MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "WHERE d.id = {dancerId} " +
                        "WITH v as v " +
                        "MATCH (d:Dancer)-[:DANCES]->(v) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs";


        return getMultipleVideos(query, ImmutableMap.of("dancerId", dancerId));
    }

    @Override
    public Set<String> exist(Set<String> ids) {
        final ImmutableMap<String, Object> params = ImmutableMap.of("ids", ImmutableList.copyOf(ids));
        final String query = "MATCH (v:Video) WHERE v.id IN {ids} RETURN v.id as id";
        final Result result = graphDb.execute(query, params);
        return IteratorUtil.asSet(result.columnAs("id"));
    }

    /**
     * Find videos that don't have any dancers assigned.
     *
     * @return list of videos
     */
    @Override
    public List<VideoResponse> needsReview() {
        final String query = "MATCH (v:Video) " +
                "WHERE NOT (:Dancer)-[:DANCES]->(v) " +
                "RETURN v, [] as dancers, [] as songs";


        return getMultipleVideos(query, ImmutableMap.of());
    }


    final private Set<String> allowedParameters = ImmutableSet.of("recordedAt");
    @Override
    public void updateField(String id, String field, String value) {
        if (!allowedParameters.contains(field)) {
            throw new NoSuchElementException(String.format("Invalid parameter: %s", field));
        }
        String query = String.format("MATCH (v:Video {id: {id}}) " +
                "SET v.%s = {value} " +
                "RETURN v", field);

        final ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", id,
                "value", value);


        try (
                Transaction tx = graphDb.beginTx();
                final Result result = graphDb.execute(query, params)
        ) {
            tx.success();
        }

    }

    @Override
    public void markComplete(String id) {
        final String query = "MATCH (v:Video {id: {id}}) " +
                "SET v.complete = true " +
                "RETURN v ";

        final ImmutableMap<String, Object> params = ImmutableMap.of("id", id);
        try (
                Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)
        ) {
            tx.success();
        }
    }

    @SuppressWarnings("unchecked")
    private List<VideoResponse> getMultipleVideos(String query, Map<String, Object> params) {
        List<VideoResponse> videos = Lists.newArrayList();
        try (Transaction tx = this.graphDb.beginTx()) {
            final Result result = this.graphDb.execute(query, params);

            while (result.hasNext()) {
                final Map<String, Object> next = result.next();
                final Node videoNode = (Node) next.get("v");
                final VideoResponse video = videoResponseFromNode(videoNode);
                video.setDancers(Sets.newHashSet((Iterable<String>) next.get("dancers")));
                video.setSongs(Sets.newHashSet((Iterable<Node>) next.get("songs"))
                        .stream()
                        .map(Neo4jSongService::mapNode)
                        .collect(Collectors.toList())
                );
                videos.add(video);
            }
            tx.success();
        }
        return videos;
    }


}
