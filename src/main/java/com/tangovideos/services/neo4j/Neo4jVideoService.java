package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
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
import java.util.Set;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;

    public Neo4jVideoService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public boolean exists(String videoId) {
        try(Transaction tx = graphDb.beginTx()){
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
            node.setProperty("id", video.getId());
            node.setProperty("addedAt", Instant.now().getEpochSecond());
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
        video.setTitle(node.getProperty("title").toString());
        video.setAddedAt(node.getProperty("addedAt").toString());
        return video;
    }

    @Override
    public List<VideoResponse> list() {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "RETURN v, collect(d.id) as dancers " +
                        "ORDER BY v.addedAt DESC";

        return getMultipleVideos(query, ImmutableMap.of());
    }

    @Override
    public List<VideoResponse> list(int skip, int limit) {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "RETURN v, collect(d.id) as dancers " +
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

        try(Transaction tx = graphDb.beginTx()){
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
                        "MATCH (d:Dancer)-[:DANCES]->(v)" +
                        "RETURN v, collect(d.id) as dancers";


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
                        "RETURN v, [] as dancers";


        return getMultipleVideos(query, ImmutableMap.of());
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
                videos.add(video);
            }
            tx.success();
        }
        return videos;
    }


}
