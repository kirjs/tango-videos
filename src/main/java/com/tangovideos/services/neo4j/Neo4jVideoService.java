package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.models.Video;
import com.tangovideos.models.VideoResponse;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;
    private final YoutubeService youtubeService;
    private DancerService dancerService;

    public Neo4jVideoService(GraphDatabaseService graphDb, YoutubeService youtubeService, DancerService dancerService) {
        this.graphDb = graphDb;
        this.youtubeService = youtubeService;
        this.dancerService = dancerService;
    }


    @Override
    public boolean videoExists(String videoId) {
        return this.graphDb.findNodes(Labels.VIDEO.label, "id", videoId).hasNext();
    }

    @Override
    public void addVideo(String videoId, Node user) {
        try (Transaction transaction = graphDb.beginTx()) {
            if (videoExists(videoId)) {
                throw new RuntimeException("VideoExists");
            }

            final Video videoInfo = youtubeService.getVideoInfo(videoId);

            if (videoInfo == null) {
                throw new RuntimeException("VideoDoesNotExistOnYoutube");
            }

            final Node node = graphDb.createNode(Labels.VIDEO.label);
            node.setProperty("title", videoInfo.getTitle());
            node.setProperty("publishedAt", videoInfo.getPublishedAt());
            node.setProperty("id", videoInfo.getId());
            user.createRelationshipTo(node, Relationships.ADDED);
            transaction.success();
        }
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

    public static VideoResponse videoResponseFromNode(Node node){
        VideoResponse video = new VideoResponse();
        video.setId((String) node.getProperty("id"));
        video.setPublishedAt((String) node.getProperty("publishedAt"));
        video.setTitle((String) node.getProperty("title"));
        return video;
    }

    @Override
    public List<VideoResponse> list() {
        List<VideoResponse> videos = Lists.newArrayList();
        try (Transaction tx = this.graphDb.beginTx()) {
            String query = "MATCH (v:Video) " +
                    "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                    "RETURN v, collect(d.id) as dancers";

            final Result result = this.graphDb.execute(query);

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

    @Override
    public Set<String> addDancer(String videoId, String dancerId) {
        final Node performer = this.dancerService.insertOrGetNode(dancerId);
        final Node video = this.get(videoId);
        try (Transaction tx = this.graphDb.beginTx()) {

            final ImmutableMap<String, Object> params = ImmutableMap.of("dancerId", dancerId, "videoId", videoId);
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    " WHERE d.id = {dancerId} AND v.id = {videoId}" +
                    " RETURN v.id";

            if (!this.graphDb.execute(query, params).hasNext()) {
                performer.createRelationshipTo(video, Relationships.DANCES);
            }


            tx.success();
        }
        return this.dancerService.getForVideo(videoId);
    }
}
