package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.YoutubeService;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Neo4jChannelServiceTest extends EasyMockSupport {


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



    @Test
    public void testFetchAllVideos() throws Exception {
        VideoService videoService = new Neo4jVideoService(graphDb);

        final String channelId = "channelId";
        channelService.addChannel(generateFakeChannel(channelId));

        YoutubeService youtubeService = createMock(YoutubeService.class);

        final String videoId = "videoId";
        final Video video = TestHelpers.generateFakeVideo(videoId);
        final Video video2 = TestHelpers.generateFakeVideo("videoId2");
        final Video existingVideo = TestHelpers.generateFakeVideo("IExist");
        videoService.addVideo(existingVideo);

        expect(youtubeService.fetchChannelVideos(channelId, 0L))
                .andReturn(ImmutableList.of(video, video2, existingVideo));



        replay(youtubeService);
        final long newVideos = channelService.fetchAllVideos(youtubeService, videoService, channelId);
        assertEquals(newVideos, 2);
        assertTrue(videoService.exists(videoId));
        final Channel channel = channelService.get(channelId);
        assertEquals(channel.getLastUpdated(), Instant.now().getEpochSecond());

    }
}
