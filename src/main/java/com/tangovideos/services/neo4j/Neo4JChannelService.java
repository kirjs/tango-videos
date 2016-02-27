package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Channel;
import com.tangovideos.services.Interfaces.ChannelService;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.time.Instant;
import java.util.List;
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
        final String query = "MATCH (c:Channel) RETURN c ";

        List<Channel> channels;
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
            channels = IteratorUtil.<Node>asList(result.columnAs("c"))
                    .stream()
                    .map(this::mapNodeToChannel)
                    .collect(Collectors.toList());
            tx.success();
        }
        return channels;
    }

    private Channel mapNodeToChannel(Node a) {
        final Channel channel = new Channel(a.getProperty("id").toString());
        channel.setTitle(a.getProperty("title").toString());
        channel.setUploadPlaylistId(a.getProperty("uploadPlaylistId").toString());
        channel.setLastUpdated(
                Long.parseLong(a.getProperty("lastUpdated", "0").toString(), 10)
        );
        return channel;
    }

    @Override
    public Channel get(String id) {
        final String query = "MATCH (c:Channel {id: {id}}) return c";
        final ImmutableMap<String, Object> params = of("id", id);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            final ResourceIterator<Node> nodes = result.<Node>columnAs("c");
            if (nodes.hasNext()) {
                return this.mapNodeToChannel(nodes.next());
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
    public int fetchAllVideos(YoutubeService youtubeService, String channelId) {
        Channel channel = get(channelId);


        return 0;
    }
}
