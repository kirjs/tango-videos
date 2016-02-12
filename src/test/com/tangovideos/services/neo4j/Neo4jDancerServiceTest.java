package com.tangovideos.services.neo4j;

import com.tangovideos.data.Labels;
import com.tangovideos.models.Dancer;
import org.easymock.EasyMockSupport;
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

public class Neo4jDancerServiceTest extends EasyMockSupport {

    private GraphDatabaseService graphDb;
    private Neo4jDancerService neo4jDancerService;


    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        neo4jDancerService = new Neo4jDancerService(graphDb);
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
            Node node = neo4jDancerService.insertOrGetNode(id);
            assertEquals(node.getProperty("id"), id);

            List<Node> nodes = IteratorUtil.asList(graphDb.findNodes(Labels.DANCER.label));
            assertEquals(1, nodes.size());
            assertEquals(nodes.get(0).getProperty("id"), id);

            // If node exists, returns existing node
            node = neo4jDancerService.insertOrGetNode(id);
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
        TestHelpers.addVideoAndDancer(graphDb, videoID, dancerId);

        try (Transaction tx = graphDb.beginTx()) {
            final Dancer dancer = this.neo4jDancerService.get(dancerId);
            assertEquals(dancer.getName(), dancerId);
            tx.success();
        }
    }

    @Test
    public void testGet() {
        try (Transaction tx = graphDb.beginTx()) {
            this.neo4jDancerService.insertOrGetNode("one");
            final Dancer dancer = this.neo4jDancerService.get("one");
            assertEquals(dancer.getName(), "one");
            assertEquals(dancer.getVideos().size(), 0);
            tx.success();
        }
    }

    @Test
    public void testList() throws Exception {
        final String dancerId = "dancerId";
        TestHelpers.addVideoAndDancer(graphDb, "videoId", dancerId);
        TestHelpers.addVideoAndDancer(graphDb, "video2Id", dancerId);
        // If a dancer have no videsh, they should not be in the list
        neo4jDancerService.insertOrGetNode("A random dude with no videos");
        try (Transaction tx = graphDb.beginTx()) {
            final List<Dancer> list = neo4jDancerService.list();

            assertEquals(1, list.size());
            assertEquals(dancerId, list.get(0).getName());
            tx.success();
        }

    }


    @Test
    public void testRemoveFromVideo() throws Exception {
        final String dancerId = "DancerId";
        final String videoId = "videoID";


        try (Transaction tx = graphDb.beginTx()) {
            final Node video = TestHelpers.addVideo(graphDb, videoId);
            final Node dancer = neo4jDancerService.insertOrGetNode(dancerId);
            neo4jDancerService.addToVideo(dancer,video);


            assertEquals(1,  neo4jDancerService.getForVideo(videoId).size());
            neo4jDancerService.removeFromVideo(dancer, video);
            assertEquals(0,  neo4jDancerService.getForVideo(videoId).size());


            tx.success();
        }


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

            final List<Dancer> list = neo4jDancerService.list(0, 100, Neo4jDancerService.SortBy.VIDEO_COUNT);


            assertEquals(3,  list.size());
            assertEquals(4, list.get(0).getVideos().size());


            tx.success();
        }
    }

    @Test
    public void testAddToVideo() throws Exception {

    }

    @Test
    public void testInsertOrGetNode() throws Exception {

    }
}