package com.tangovideos.resources;

import com.tangovideos.models.Video;
import com.tangovideos.services.neo4j.Neo4jChannelService;
import com.tangovideos.services.neo4j.Neo4jDancerService;
import com.tangovideos.services.neo4j.Neo4jVideoService;
import com.tangovideos.services.neo4j.TestHelpers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class VideoResourceTest {

    private GraphDatabaseService graphDb;
    private Neo4jChannelService channelService;
    private Neo4jVideoService videoService;
    private Neo4jDancerService dancerService;
    private VideoResource videoResource;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        channelService = new Neo4jChannelService(graphDb);
        dancerService = new Neo4jDancerService(graphDb);
        videoService = new Neo4jVideoService(graphDb);
        videoResource = new VideoResource(dancerService, videoService, channelService);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }



    @Test
    public void testAdd() throws Exception {
        final String videoId = "testVideo";
        final Video testVideo = TestHelpers.generateFakeVideo(videoId);
        videoService.addVideo(testVideo);

        final List<Video> list = videoService.list();
        final Video video = list.get(0);
        assertEquals(videoId, video.getId());
    }
}
