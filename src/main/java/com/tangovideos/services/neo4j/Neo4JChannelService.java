package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Channel;
import com.tangovideos.services.Interfaces.ChannelService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
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
        final String query = "MERGE (c:Channel {" +
                "   id: {channelId}, " +
                "   title: {title}, " +
                "   uploadPlaylistId: {uploadPlaylistId}" +
                "}) " +
                "ON CREATE SET c.addedAt = {addedAt} " +
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
                    .map(a -> {
                        final Channel channel = new Channel(a.getProperty("id").toString());
                        channel.setTitle(a.getProperty("title").toString());
                        channel.setUploadPlaylistId(a.getProperty("uploadPlaylistId").toString());
                        return channel;
                    })
                    .collect(Collectors.toList());
            tx.success();
        }
        return channels;
    }
}
