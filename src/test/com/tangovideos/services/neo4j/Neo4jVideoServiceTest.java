package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.models.VideoResponse;
import com.tangovideos.services.Interfaces.VideoService;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;
import java.util.NoSuchElementException;

import static com.google.common.collect.ImmutableMap.of;
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
        final Song song = new Song();
        String year = "1991";
        song.setYear(year);
        song.setName("name");

        try (Transaction tx = graphDb.beginTx()) {
            TestHelpers.addVideoAndDancer(graphDb, videoId, dancerId);
            TestHelpers.addSongToVideo(graphDb, videoId, song);
            final List<VideoResponse> list = videoService.list();
            assertEquals(list.size(), 1);
            final VideoResponse video = list.get(0);
            assertEquals(video.getId(), videoId);
            assertEquals(video.getSongs().get(0).getYear(), year);
            assertEquals(video.getDancers().iterator().next(), dancerId);


            tx.success();
        }

    }


    @Test
    public void testListByDancer() throws Exception {
        final String videoId = "videoId";
        final String dancerId = "dancerId";
        final String otherDancer = "otherDancer";
        final String thirdDancerId = "thirdDancer";

        try (Transaction tx = graphDb.beginTx()) {

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
            tx.success();
        }
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
    public void testNeedsReviewDancers() throws Exception {
        final Neo4jSongService songService = new Neo4jSongService(graphDb);
        final Neo4jDancerService dancerService = new Neo4jDancerService(graphDb);

        final String dancerId = "dancerId";
        final String videoId0 = "videoId0";
        final String noDancersId = "noDancers";
        final String videoId2 = "videoId2";
        final String videoId3 = "videoId3";

        try (Transaction tx = graphDb.beginTx()) {

            final Node video0 = TestHelpers.addVideo(graphDb, videoId0);
            songService.updateField(videoId0, 0, "name", "name");
            songService.updateField(videoId0, 0, "genre", "genre");
            songService.updateField(videoId0, 0, "year", "2000");
            songService.updateField(videoId0, 0, "orquestra", "orquestra");
            dancerService.addToVideo(video0, dancerService.insertOrGetNode("SuperDancer"));
            dancerService.addToVideo(video0, dancerService.insertOrGetNode("OtherDancer"));


            // No dancer
            final Node video1 = TestHelpers.addVideo(graphDb, noDancersId);
            songService.updateField(noDancersId, 0, "name", "name");
            songService.updateField(noDancersId, 0, "genre", "genre");
            songService.updateField(noDancersId, 0, "year", "2000");
            songService.updateField(noDancersId, 0, "orquestra", "orquestra");


            final Node video2 = TestHelpers.addVideo(graphDb, videoId2);
            songService.updateField(videoId2, 0, "name", "name");
            songService.updateField(videoId2, 0, "genre", "genre");
            songService.updateField(videoId2, 0, "year", "2000");
            songService.updateField(videoId2, 0, "orquestra", "orquestra");
            dancerService.addToVideo(video2, dancerService.insertOrGetNode("SuperDancer"));
            dancerService.addToVideo(video2, dancerService.insertOrGetNode("OtherDancer"));


            final Node video3 = TestHelpers.addVideo(graphDb, videoId3);
            songService.updateField(videoId3, 0, "name", "name");
            songService.updateField(videoId3, 0, "genre", "genre");
            songService.updateField(videoId3, 0, "year", "2000");
            songService.updateField(videoId3, 0, "orquestra", "orquestra");
            dancerService.addToVideo(video3, dancerService.insertOrGetNode("SuperDancer"));
            dancerService.addToVideo(video3, dancerService.insertOrGetNode("OtherDancer"));


            // Find videos with no dancers
            final ImmutableMap<String, Boolean> noDancers =
                    of("dancers", true, "song", false, "orquestra", false, "genre", false, "year", false);
            final List<VideoResponse> list = videoService
                    .needsReview(noDancers);
            assertEquals(list.size(), 1);
            final VideoResponse video = list.get(0);
            assertEquals(video.getId(), noDancersId);
            assertEquals(video.getDancers().size(), 0);
            tx.success();
        }
    }


    @Test
    public void testListWithPages() throws Exception {
        final String dancerId = "dancerId";
        final String videoId0 = "videoId0";
        final String videoId1 = "videoId1";
        final String videoId2 = "videoId2";
        final String videoId3 = "videoId3";

        try (Transaction tx = graphDb.beginTx()) {
            TestHelpers.addVideoAndDancer(graphDb, videoId0, dancerId);
            TestHelpers.addVideoAndDancer(graphDb, videoId1, dancerId);
            TestHelpers.addVideoAndDancer(graphDb, videoId2, dancerId);
            TestHelpers.addVideoAndDancer(graphDb, videoId3, dancerId);

            List<VideoResponse> list = videoService.list(0, 1);
            assertEquals(list.size(), 1);
            assertEquals(list.get(0).getId(), videoId3);

            list = videoService.list(2, 2);
            assertEquals(list.size(), 2);
            assertEquals(list.get(0).getId(), videoId1);
            assertEquals(list.get(1).getId(), videoId0);
            tx.success();
        }

    }

    @Test
    public void testExists() throws Exception {

    }

    @Test
    public void testHideVideo() throws Exception {
        final String videoId = "videoId0";
        TestHelpers.addVideo(graphDb, videoId);
        assertEquals(true, videoService.exists(videoId));
        videoService.hide(videoId, true);
        assertEquals(false, videoService.exists(videoId));
        videoService.hide(videoId, false);
        assertEquals(true, videoService.exists(videoId));
    }

    @Test
    public void testHide() throws Exception {

    }

    @Test
    public void testUpdateField() throws Exception {
        final String videoId = "videoId0";
        final String newValue = "12345";
        try (Transaction tx = graphDb.beginTx()) {
            final Node node = TestHelpers.addVideo(graphDb, videoId);
            videoService.updateField(videoId, "recordedAt", newValue);
            assertEquals(node.getProperty("recordedAt"), newValue);
            tx.success();
        }
    }

    @Test(expected = NoSuchElementException.class)
    public void testUpdateFieldUnexistingParameter() {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);

        videoService.updateField(videoId, "leg", "123");
    }

    @Test
    public void testMarkComplete() throws Exception {
        try (Transaction tx = graphDb.beginTx()) {
            final String videoId = "videoId0";
            final Node video = TestHelpers.addVideo(graphDb, videoId);
            videoService.markComplete(videoId, true);
            assertEquals(true, video.getProperty("complete"));
            videoService.markComplete(videoId, false);
            assertEquals(false, video.getProperty("complete"));
            tx.success();
        }
    }
}
