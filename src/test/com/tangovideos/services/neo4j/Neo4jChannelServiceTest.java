package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Channel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class Neo4jChannelServiceTest {


    private GraphDatabaseService graphDb;
    private Neo4jChannelService channelService;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        channelService = new Neo4jChannelService(graphDb);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testAddChannel() throws Exception {
        String id = "superChannel";
        final String title = "title";
        final String uploadPlaylistId = "123";

        final Channel fakeChannel = new Channel(id);
        fakeChannel.setTitle(title);
        fakeChannel.setUploadPlaylistId(uploadPlaylistId);
        channelService.addChannel(fakeChannel);

        final String query = "MATCH (c:Channel {id: {id}}) RETURN c";
        final ImmutableMap<String, Object> params = ImmutableMap.of("id", id);
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            final Map<String, Object> channel = result.<Node>columnAs("c").next().getAllProperties();
            assertEquals(channel.get("id"), id);
            assertEquals(channel.get("title"), title);
            assertEquals(channel.get("uploadPlaylistId"), uploadPlaylistId);
            tx.success();
        }

        // Make sure adding the same channel doesn't duplicate it
        channelService.addChannel(fakeChannel);
        assertEquals(channelService.list().size(), 1);

    }


    @Test
    public void testGet() {
        final String id = "one";
        final Channel one = generateFakeChannel(id);
        channelService.addChannel(one);

        final Channel channel = channelService.get(id);

        assertEquals(channel.getId(), one.getId());
        assertEquals(channel.getTitle(), one.getTitle());
        assertEquals(channel.getUploadPlaylistId(), one.getUploadPlaylistId());
        assertEquals(channel.getLastUpdated(), 0);
    }

    @Test
    public void testList() throws Exception {
        final Channel one = generateFakeChannel("one");
        channelService.addChannel(one);

        final Channel two = new Channel("two");
        two.setTitle("title");
        two.setUploadPlaylistId("title");
        channelService.addChannel(two);

        final List<Channel> list = channelService.list();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getId(), "one");
        assertEquals(list.get(0).getTitle(), "title");
        assertEquals(list.get(0).getUploadPlaylistId(), "id");
    }

    private Channel generateFakeChannel(String id) {
        final Channel one = new Channel(id);
        one.setTitle("title");
        one.setUploadPlaylistId("id");
        return one;
    }

    @Test
    public void testUpdate() throws Exception {
        final String id = "test";
        final Channel channel = generateFakeChannel(id);
        channelService.addChannel(channel);
        channel.setLastUpdated(123L);
        channelService.update(channel);

        final Channel savedChannel = channelService.get("test");

        assertEquals(savedChannel.getLastUpdated(), channel.getLastUpdated());
    }

    @Test
    public void testUpdate1() throws Exception {

    }
}
