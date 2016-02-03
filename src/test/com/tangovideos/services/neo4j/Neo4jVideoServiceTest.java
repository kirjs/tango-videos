package com.tangovideos.services.neo4j;

import com.tangovideos.models.VideoResponse;
import com.tangovideos.services.Interfaces.VideoService;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Neo4jVideoServiceTest extends EasyMockSupport {

    private GraphDatabaseService graphDb;
    private Neo4jVideoService neo4jVideoService;
    private VideoService videoService;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        videoService = new Neo4jVideoService(graphDb);

    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }


    @Test
    public void testVideoExists() throws Exception {

    }

    @Test
    public void testAddVideo() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testVideoResponseFromNode() throws Exception {

    }

    @Test
    public void testList() throws Exception {
        final String videoId = "videoId";
        final String dancerId = "dancerId";
        TestHelpers.addVideoAndDancer(graphDb, videoId, dancerId);
        final List<VideoResponse> list = videoService.list();
        assertEquals(list.size(), 1);
        final VideoResponse video = list.get(0);
        assertEquals(video.getId(), videoId);
        assertEquals(video.getDancers().iterator().next(), dancerId);
    }


    @Test
    public void testListByDancer() throws Exception {
        final String videoId = "videoId";
        final String dancerId = "dancerId";
        final String otherDancer = "otherDancer";
        final String thirdDancerId = "thirdDancer";

        // Add a two videos with two dancers
        TestHelpers.addVideoAndDancer(graphDb, videoId, dancerId);
        TestHelpers.addVideoAndDancer(graphDb, "otherVideo", otherDancer);

        // Add another dancer to the first video
        final Neo4jDancerService neo4jDancerService = new Neo4jDancerService(graphDb);

        neo4jDancerService.addToVideo(
                neo4jDancerService.insertOrGetNode(thirdDancerId),
                videoService.get(videoId)
        );


        final List<VideoResponse> list = videoService.listByDancer(dancerId);
        assertEquals(list.size(), 1);
        final VideoResponse video = list.get(0);
        assertEquals(video.getId(), videoId);
        assertEquals(video.getDancers().size(), 2);
        assertEquals(video.getDancers().iterator().next(), dancerId);
    }


    @Test
    public void testAddDancer() throws Exception {

    }
}