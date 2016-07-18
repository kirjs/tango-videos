package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Dancer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Neo4jDancerServiceTest {

    private GraphDatabaseService graphDb;
    private Neo4jDancerService dancerService;


    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        dancerService = new Neo4jDancerService(graphDb);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testGetNode() throws Exception {
        try (Transaction tx = graphDb.beginTx()) {
            final String id = "supernode";

            assertEquals(0, IteratorUtil.count(graphDb.findNodes(Labels.DANCER.label)));

            // If node does not exist inserts a copy and returns
            Node node = dancerService.insertOrGetNode(id);
            assertEquals(node.getProperty("id"), id);

            List<Node> nodes = IteratorUtil.asList(graphDb.findNodes(Labels.DANCER.label));
            assertEquals(1, nodes.size());
            assertEquals(nodes.get(0).getProperty("id"), id);

            // If node exists, returns existing node
            node = dancerService.insertOrGetNode(id);
            assertEquals(node.getProperty("id"), id);


            nodes = IteratorUtil.asList(graphDb.findNodes(Labels.DANCER.label));
            assertEquals(1, nodes.size());
            tx.success();
        }
    }

    @Test
    public void testGetForVideo() throws Exception {

        final String dancerId = "DancerId";
        final String videoID = "videoID";


        try (Transaction tx = graphDb.beginTx()) {
            TestHelpers.addVideoAndDancer(graphDb, videoID, dancerId);
            final Dancer dancer = this.dancerService.get(dancerId);
            assertEquals(dancer.getName(), dancerId);
            tx.success();
        }
    }

    @Test
    public void testGet() {
        try (Transaction tx = graphDb.beginTx()) {
            this.dancerService.insertOrGetNode("one");
            final Dancer dancer = this.dancerService.get("one");
            assertEquals(dancer.getName(), "one");
            assertEquals(dancer.getVideos().size(), 0);
            tx.success();
        }
    }

    @Test
    public void testList() throws Exception {
        final String dancerId = "dancerId";
        // If a dancer have no videsh, they should not be in the list
        dancerService.insertOrGetNode("A random dude with no videos");
        try (Transaction tx = graphDb.beginTx()) {
            TestHelpers.addVideoAndDancer(graphDb, "videoId", dancerId);
            TestHelpers.addVideoAndDancer(graphDb, "video2Id", dancerId);

            final List<Dancer> list = dancerService.list();

            assertEquals(1, list.size());
            assertEquals(dancerId, list.get(0).getName());
            tx.success();
        }

    }


    @Test
    public void testAddTovideo() {
        final String dancerId = "DancerId";
        final String videoId = "videoID";

        final Node video = TestHelpers.addVideo(graphDb, videoId);
        final Node dancer = dancerService.insertOrGetNode(dancerId);
        dancerService.addToVideo(dancer, video);

        assertEquals(1, dancerService.getForVideo(videoId).size());
        assertEquals(1, IteratorUtil.count(graphDb.execute("MATCH (d:Dancer)-[:DANCES]->(v:Video) return v").columnAs("v")));
    }

    @Test
    public void testRemoveFromVideo() throws Exception {
        final String dancerId = "DancerId";
        final String videoId = "videoID";

        final Node video = TestHelpers.addVideo(graphDb, videoId);
        final Node dancer = dancerService.insertOrGetNode(dancerId);
        dancerService.addToVideo(dancer, video);


        assertEquals(1, dancerService.getForVideo(videoId).size());
        dancerService.removeFromVideo(dancer, video);
        assertEquals(0, dancerService.getForVideo(videoId).size());
    }

    @Test
    public void testListWithParams() throws Exception {
        final String dancerId = "DancerId";
        final String videoId = "videoID";


        try (Transaction tx = graphDb.beginTx()) {
            TestHelpers.addVideoAndDancer(graphDb, "video-1", "dancer0");
            TestHelpers.addVideoAndDancer(graphDb, "video-0", "dancer0");
            TestHelpers.addVideoAndDancer(graphDb, "video", dancerId);
            TestHelpers.addVideoAndDancer(graphDb, "video1", dancerId);
            TestHelpers.addVideoAndDancer(graphDb, "video2", dancerId);
            TestHelpers.addVideoAndDancer(graphDb, "video3", dancerId);
            TestHelpers.addVideoAndDancer(graphDb, "video4", "dancer2");

            final List<Dancer> list = dancerService.list(0, 100, Neo4jDancerService.SortBy.VIDEO_COUNT);


            assertEquals(3, list.size());

            // We only send 3 videos per dancer for the preview.
            assertEquals(3, list.get(0).getVideos().size());
            assertEquals(4, list.get(0).getVideoCount());


            tx.success();
        }
    }

    @Test
    public void testAddToVideo() throws Exception {

    }

    @Test
    public void testInsertOrGetNode() throws Exception {

    }

    @Test
    public void testGetaAllDancersByVideo() throws Exception {
        TestHelpers.addVideoAndDancer(graphDb, "video-1", "dancer0");
        TestHelpers.addVideoAndDancer(graphDb, "video-2", "dancer0");
        assertEquals(ImmutableSet.of(ImmutableSet.of("dancer0")),
                dancerService.getaAllDancersByVideo());

    }

    @Test
    public void testAddPseudonym() throws Exception {
        final String dancerId = "dancer0";
        final String pseudonym = "name";
        final String OtherPseudonym = "otherName";

        TestHelpers.addVideoAndDancer(graphDb, "video-1", dancerId);
        dancerService.addPseudonym(dancerId, pseudonym);
        dancerService.addPseudonym(dancerId, OtherPseudonym);

        final Dancer dancer = dancerService.get(dancerId);

        assertEquals(dancer.getPseudonyms(), ImmutableSet.of(
                pseudonym,
                OtherPseudonym
        ));
    }


    @Test
    public void testRemovePseudonym() throws Exception {
        final String dancerId = "dancer0";
        final String pseudonym = "name";

        TestHelpers.addVideoAndDancer(graphDb, "video-1", dancerId);
        dancerService.addPseudonym(dancerId, pseudonym);
        dancerService.removePseudonym(dancerId, pseudonym);

        final Dancer dancer = dancerService.get(dancerId);
        assertEquals(ImmutableSet.of(), dancer.getPseudonyms());
    }



    @Test
    public void testGetPseudonyms() throws Exception {
        final String dancer0 = "dancer0";
        final String dancer1 = "dancer1";
        final String pseudonym = "name";

        TestHelpers.addVideoAndDancer(graphDb, "video-1", dancer0);
        TestHelpers.addVideoAndDancer(graphDb, "video-2", dancer1);
        
        dancerService.addPseudonym(dancer0, "name");
        assertEquals(ImmutableMap.of(pseudonym, ImmutableSet.of(dancer0)), dancerService.getPseudonyms());

        dancerService.addPseudonym(dancer1, "name");
        assertEquals(ImmutableMap.of(pseudonym, ImmutableSet.of(dancer0, dancer1)), dancerService.getPseudonyms());
    }
}
