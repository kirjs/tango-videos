package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
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

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;

    public Neo4jVideoService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public boolean videoExists(String videoId) {
        return this.graphDb.findNodes(Labels.VIDEO.label, "id", videoId).hasNext();
    }

    @Override
    public Node addVideo(@NotNull Video video) {
        final Node node;
        try (Transaction transaction = graphDb.beginTx()) {
            if (videoExists(video.getId())) {
                throw new RuntimeException("VideoExists");
            }

            node = graphDb.createNode(Labels.VIDEO.label);
            node.setProperty("title", video.getTitle());
            node.setProperty("publishedAt", video.getPublishedAt());
            node.setProperty("id", video.getId());
            transaction.success();
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
        video.setId((String) node.getProperty("id"));
        video.setPublishedAt((String) node.getProperty("publishedAt"));
        video.setTitle((String) node.getProperty("title"));
        return video;
    }

    @Override
    public List<VideoResponse> list() {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "RETURN v, collect(d.id) as dancers";

        return getMultipleVideos(query, ImmutableMap.of());
    }

    @Override
    public List<VideoResponse> listByDancer(String dancerId) {
        final String query =
                "MATCH (v:Video) " +
                        "MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "WHERE d.id = {dancerId} " +
                        "RETURN v, collect(d.id) as dancers";

        return getMultipleVideos(query, ImmutableMap.of("dancerId", dancerId));
    }


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
