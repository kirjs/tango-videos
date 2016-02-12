package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
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
        TestHelpers.reset();

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

    @Test
    public void testExist() throws Exception {
        TestHelpers.addVideo(graphDb, "one");
        TestHelpers.addVideo(graphDb, "three");
        TestHelpers.addVideo(graphDb, "five");

        assertEquals(ImmutableSet.of("one", "three"),
                videoService.exist(ImmutableSet.of("one", "two", "three", "four"))
        );
    }


    @Test
    public void testNeedsReview() throws Exception {
        final String videoId = "videoId";
        TestHelpers.addVideoAndDancer(graphDb, "someVideo", "dancerId");
        TestHelpers.addVideo(graphDb, videoId);

        final List<VideoResponse> list = videoService.needsReview();
        assertEquals(list.size(), 1);
        final VideoResponse video = list.get(0);
        assertEquals(video.getId(), videoId);
        assertEquals(video.getDancers().size(), 0);
    }

    @Test
    public void testListWithPages() throws Exception {
        final String dancerId = "dancerId";
        final String videoId0 = "videoId0";
        final String videoId1 = "videoId1";
        final String videoId2 = "videoId2";
        final String videoId3 = "videoId3";
        TestHelpers.addVideoAndDancer(graphDb, videoId0, dancerId);
        TestHelpers.addVideoAndDancer(graphDb, videoId1, dancerId);
        TestHelpers.addVideoAndDancer(graphDb, videoId2, dancerId);
        TestHelpers.addVideoAndDancer(graphDb, videoId3, dancerId);

        List<VideoResponse> list = videoService.list(0,1);
        assertEquals(list.size(), 1);
        assertEquals(list.get(0).getId(), videoId3);

        list = videoService.list(2,2);
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getId(), videoId1);
        assertEquals(list.get(1).getId(), videoId0);


    }

    @Test
    public void testExists() throws Exception {

    }

    @Test
    public void testHideVideo() throws Exception {
        final String videoId = "videoId0";
        TestHelpers.addVideo(graphDb, videoId);
        assertEquals(true, videoService.exists(videoId));
        videoService.hideVideo(videoId);
        assertEquals(false, videoService.exists(videoId));


    }
}
