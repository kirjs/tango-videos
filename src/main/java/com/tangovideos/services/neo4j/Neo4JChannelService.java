package com.tangovideos.services.neo4j;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Channel;
import com.tangovideos.services.Interfaces.ChannelService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined.CombinedVideoService;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;


public class Neo4jChannelService implements ChannelService {
    private final GraphDatabaseService graphDb;


    public Neo4jChannelService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public Node addChannel(Channel channel) {
        final String query = "MERGE (c:Channel {id: {channelId}}) " +
                "ON CREATE SET c.addedAt = {addedAt}, " +
                "c.lastUpdated = 0, " +
                "c.title = {title}, " +
                "c.uploadPlaylistId = {uploadPlaylistId} " +
                "RETURN c";

        final ImmutableMap<String, Object> params = of(
                "channelId", channel.getId(),
                "addedAt", Instant.now().getEpochSecond(),
                "title", channel.getTitle(),
                "uploadPlaylistId", channel.getUploadPlaylistId()
        );
        Node result;
        try (Transaction tx = graphDb.beginTx(); Result queryResult = graphDb.execute(query, params)) {
            result = (Node) queryResult.columnAs("c").next();
            tx.success();
        }
        return result;
    }


    @Override
    public List<Channel> list() {
        final String query = "MATCH (c:Channel) " +
                "OPTIONAL MATCH (v:Video)-[r:IS_IN]->(c:Channel) " +
                "RETURN c, count(r) as videos";

        List<Channel> channels;
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {


            channels = IteratorUtil.asList(result)
                    .stream()
                    .map(e -> this.mapNodeToChannel((Node) e.get("c"), (Long) e.get("videos")))
                    .collect(Collectors.toList());
            tx.success();
        }
        return channels;
    }

    private Channel mapNodeToChannel(Node a, Long videos) {
        final Channel channel;
        try (Transaction tx = graphDb.beginTx()) {
            channel = new Channel(a.getProperty("id").toString());
            channel.setTitle(a.getProperty("title").toString());
            channel.setUploadPlaylistId(a.getProperty("uploadPlaylistId").toString());
            channel.setLastUpdated(
                    Long.parseLong(a.getProperty("lastUpdated", "0").toString(), 10)
            );
            channel.setVideosCount(videos);

            tx.success();
        }
        return channel;
    }


    @Override
    public Channel get(String id) {
        final String query = "MATCH (c:Channel {id: {id}})" +
                "OPTIONAL MATCH (v:Video)-[r]-(c) " +
                "RETURN c, v, count(r) as videos";
        final ImmutableMap<String, Object> params = of("id", id);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {

            if (result.hasNext()) {
                final Map<String, Object> next = result.next();
                return mapNodeToChannel(
                        (Node) next.get("c"),
                        (Long) next.<Long>getOrDefault("videos", 0L)
                );
            }
            tx.success();
        }
        return null;
    }

    @Override
    public void update(Channel channel) {
        final String query = "MERGE (c:Channel {id: {channelId}}) " +
                "ON MATCH SET c.addedAt = {addedAt}, " +
                "c.lastUpdated = 0, " +
                "c.title = {title}, " +
                "c.uploadPlaylistId = {uploadPlaylistId}, " +
                "c.lastUpdated = {lastUpdated} " +
                "RETURN c";

        final ImmutableMap<String, Object> params = ImmutableMap.<String, Object>builder()
                .put("channelId", channel.getId())
                .put("addedAt", Instant.now().getEpochSecond())
                .put("title", channel.getTitle())
                .put("uploadPlaylistId", channel.getUploadPlaylistId())
                .put("lastUpdated", channel.getLastUpdated())
                .build();

        try (Transaction tx = graphDb.beginTx(); Result queryResult = graphDb.execute(query, params)) {
            tx.success();
        }
    }

    @Override
    public boolean addVideoToChannel(String videoId, String channelId) {
        final String query = "MATCH (c:Channel {id: {channelId}}) " +
                "MATCH (v:Video {id: {videoId}}) " +
                "MERGE (v)-[r:IS_IN]->(c) " +
                "RETURN r";

        final ImmutableMap<String, Object> params = of("channelId", channelId, "videoId", videoId);
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            tx.success();
            return true;
        }
    }

    @Override
    public long fetchAllVideos(YoutubeService youtubeService, CombinedVideoService combinedVideoService, String channelId) {
        Channel channel = get(channelId);
            final long lastUpdated = 0L; //channel.getLastUpdated();
        long count = youtubeService.fetchChannelVideos(channel.getUploadPlaylistId(), lastUpdated)
                .stream().filter(v -> !combinedVideoService.videoExists(v.getId()))
                .peek(combinedVideoService::addVideo)
                .count();
        channel.setLastUpdated(Instant.now().getEpochSecond());
        update(channel);
        return count;

    }

    @Override
    public boolean exists(String channelId) {
        try (Transaction tx = graphDb.beginTx()) {
            tx.success();
            return Optional
                    .fromNullable(graphDb.findNode(Labels.CHANNEL.label, "id", channelId))
                    .isPresent();
        }
    }
}
