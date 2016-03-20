package com.tangovideos.resources;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined._CombinedVideoService;
import com.tangovideos.services.neo4j.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

public class VideoResourceTest {

    private GraphDatabaseService graphDb;
    private Neo4jChannelService channelService;
    private Neo4jVideoService videoService;
    private Neo4jDancerService dancerService;
    private YoutubeService youtubeService;
    private VideoResource videoResource;
    private _CombinedVideoService combinedVideoService;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        channelService = new Neo4jChannelService(graphDb);
        dancerService = new Neo4jDancerService(graphDb);
        videoService = new Neo4jVideoService(graphDb);
        youtubeService = createMock(YoutubeService.class);
        combinedVideoService = new _CombinedVideoService(dancerService, videoService, channelService, youtubeService);
        videoResource = new VideoResource(dancerService, videoService, youtubeService, combinedVideoService);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }


    @Test
    public void testAdd() throws Exception {
        final String videoId = "testVideo";
        final Video testVideo = TestHelpers.generateFakeVideo(videoId);

        expect(youtubeService.getChannelInfoById(testVideo.getChannelId())).andReturn(
                Neo4jChannelServiceTest.generateFakeChannel(testVideo.getChannelId())
        );

        replay(youtubeService);

        videoResource.add(testVideo);

        final List<Video> list = videoService.list();
        final Video video = list.get(0);
        assertEquals(videoId, video.getId());
        assertEquals(ImmutableSet.of("fakeDancer"), video.getDancers());

        final Channel channel = channelService.get(testVideo.getChannelId());
        assertEquals(1L, channel.getVideosCount());
    }
}
