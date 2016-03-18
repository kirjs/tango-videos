package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.VideoService;
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

public class Neo4jVideoServiceTest {

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
    public void testList() throws Exception {
        final String videoId = "videoId";
        final String dancerId = "dancerId";
        final Song song = new Song();
        String year = "1991";
        song.setYear(year);
        song.setName("name");


        TestHelpers.addVideoAndDancer(graphDb, videoId, dancerId);
        TestHelpers.addSongToVideo(graphDb, videoId, song);
        final List<Video> list = videoService.list();
        assertEquals(list.size(), 1);
        final Video video = list.get(0);
        assertEquals(video.getId(), videoId);
        assertEquals(video.getSongs().get(0).getYear(), year);
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


        final List<Video> list = videoService.listByDancer(dancerId);
        assertEquals(list.size(), 1);
        final Video video = list.get(0);
        assertEquals(video.getId(), videoId);
        assertEquals(video.getDancers().size(), 2);
        assertEquals(video.getDancers().iterator().next(), dancerId);

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
        final String noSongId = "videoId2";
        final String noOrquestraId = "videoId3";
        final String noGenreId = "videoId4";
        final String noYearId = "videoId5";
        final String nonDancePreformanceVideoId = "not performance";


        final Node video0 = TestHelpers.addVideo(graphDb, videoId0);
        songService.updateField(videoId0, 0, "name", "name");
        songService.updateField(videoId0, 0, "genre", "genre");
        songService.updateField(videoId0, 0, "year", "2000");
        songService.updateField(videoId0, 0, "orquestra", "orquestra");
        dancerService.addToVideo(dancerService.insertOrGetNode("SuperDancer"), video0);
        dancerService.addToVideo(dancerService.insertOrGetNode("OtherDancer"), video0);


        // No dancer
        final Node video1 = TestHelpers.addVideo(graphDb, noDancersId);
        songService.updateField(noDancersId, 0, "name", "name");
        songService.updateField(noDancersId, 0, "genre", "genre");
        songService.updateField(noDancersId, 0, "year", "2000");
        songService.updateField(noDancersId, 0, "orquestra", "orquestra");

        // This video should be ignored
        TestHelpers.addVideo(graphDb, nonDancePreformanceVideoId);
        videoService.updateField(nonDancePreformanceVideoId, "type", "Non tango");

        // No song name
        final Node video2 = TestHelpers.addVideo(graphDb, noSongId);
        songService.updateField(noSongId, 0, "genre", "genre");
        songService.updateField(noSongId, 0, "year", "2000");
        songService.updateField(noSongId, 0, "orquestra", "orquestra");
        dancerService.addToVideo(dancerService.insertOrGetNode("SuperDancer"), video2);
        dancerService.addToVideo(dancerService.insertOrGetNode("OtherDancer"), video2);

        // No orquestra
        final Node video3 = TestHelpers.addVideo(graphDb, noOrquestraId);
        songService.updateField(noOrquestraId, 0, "name", "name");
        songService.updateField(noOrquestraId, 0, "genre", "genre");
        songService.updateField(noOrquestraId, 0, "year", "2000");
        dancerService.addToVideo(dancerService.insertOrGetNode("SuperDancer"), video3);
        dancerService.addToVideo(dancerService.insertOrGetNode("OtherDancer"), video3);


        // No genre
        final Node video4 = TestHelpers.addVideo(graphDb, noGenreId);
        songService.updateField(noGenreId, 0, "name", "name");
        songService.updateField(noGenreId, 0, "year", "2000");
        songService.updateField(noGenreId, 0, "orquestra", "orquestra");
        dancerService.addToVideo(dancerService.insertOrGetNode("SuperDancer"), video4);
        dancerService.addToVideo(dancerService.insertOrGetNode("OtherDancer"), video4);

        // No year
        final Node video5 = TestHelpers.addVideo(graphDb, noYearId);
        songService.updateField(noYearId, 0, "genre", "genre");
        songService.updateField(noYearId, 0, "name", "name");
        songService.updateField(noYearId, 0, "orquestra", "orquestra");
        dancerService.addToVideo(dancerService.insertOrGetNode("SuperDancer"), video5);
        dancerService.addToVideo(dancerService.insertOrGetNode("OtherDancer"), video5);


        final ImmutableMap<String, Boolean> needsDancers =
                of("dancers", true, "songname", false, "orquestra", false, "genre", false, "year", false);
        final ImmutableMap<String, Boolean> needsSong =
                of("dancers", false, "songname", true, "orquestra", false, "genre", false, "year", false);
        final ImmutableMap<String, Boolean> needsOrquestra =
                of("dancers", false, "songname", false, "orquestra", true, "genre", false, "year", false);
        final ImmutableMap<String, Boolean> needsGenre =
                of("dancers", false, "songname", false, "orquestra", false, "genre", true, "year", false);
        final ImmutableMap<String, Boolean> needsYear =
                of("dancers", false, "songname", false, "orquestra", false, "genre", false, "year", true);
        final ImmutableMap<String, Boolean> needsYearAndGenreAndSong =
                of("dancers", false, "songname", true, "orquestra", false, "genre", true, "year", true);

        // Find videos which need dancers
        final List<Video> listNoDancers = videoService.needsReview(needsDancers);
        assertEquals(listNoDancers.size(), 1);
        final Video videoNoDancers = listNoDancers.get(0);
        assertEquals(videoNoDancers.getId(), noDancersId);
        assertEquals(videoNoDancers.getDancers().size(), 0);

        // Find videos which need song names
        final List<Video> listNoSong = videoService.needsReview(needsSong);
        assertEquals(listNoSong.size(), 1);
        final Video videoNoSong = listNoSong.get(0);
        assertEquals(videoNoSong.getId(), noSongId);
        assertEquals(videoNoSong.getDancers().size(), 2);

        // Find videos which need song names
        final List<Video> listNoOrquestra = videoService.needsReview(needsOrquestra);
        assertEquals(listNoOrquestra.size(), 1);
        final Video videoNoOrquestra = listNoOrquestra.get(0);
        assertEquals(videoNoOrquestra.getId(), noOrquestraId);
        assertEquals(videoNoOrquestra.getDancers().size(), 2);

        // Find videos which need song names
        final List<Video> listNoGenre = videoService.needsReview(needsGenre);
        assertEquals(listNoGenre.size(), 1);
        final Video videoNoGenre = listNoGenre.get(0);
        assertEquals(videoNoGenre.getId(), noGenreId);
        assertEquals(videoNoGenre.getDancers().size(), 2);

        // Find videos which need song names
        final List<Video> listNoYear = videoService.needsReview(needsYear);
        assertEquals(listNoYear.size(), 1);
        final Video videoNoYear = listNoYear.get(0);
        assertEquals(videoNoYear.getId(), noYearId);
        assertEquals(videoNoYear.getDancers().size(), 2);

        // Find videos which need song names
        final List<Video> listNoYearGenreSong = videoService.needsReview(needsYearAndGenreAndSong);
        assertEquals(listNoYearGenreSong.size(), 3);


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

            List<Video> list = videoService.list(0, 1);
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

        final Node node = TestHelpers.addVideo(graphDb, videoId);
        videoService.updateField(videoId, "recordedAt", newValue);
        try (Transaction tx = graphDb.beginTx()) {
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
