package com.tangovideos.services.neo4j;

import com.tangovideos.data.Labels;
import com.tangovideos.models.Dancer;
import com.tangovideos.services.Interfaces.UserService;
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

import static org.junit.Assert.*;

public class Neo4jDancerServiceTest extends EasyMockSupport {

    private GraphDatabaseService graphDb;
    private Neo4jDancerService neo4jDancerService;
    private UserService userService;


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
        final String id = "one";
        TestHelpers.addVideoAndDancer(graphDb, neo4jDancerService, id);

        try(Transaction tx = graphDb.beginTx()){
            final Dancer dancer = this.neo4jDancerService.get(id);
            assertEquals(dancer.getName(), id);
            assertEquals(dancer.getVideos().size(), 1);
            tx.success();
        }
    }

    @Test
    public void testGet() {

        try(Transaction tx = graphDb.beginTx()){
            this.neo4jDancerService.insertOrGetNode("one");
            final Dancer dancer = this.neo4jDancerService.get("one");
            assertEquals(dancer.getName(), "one");
            assertEquals(dancer.getVideos().size(), 0);
            tx.success();
        }
    }

    @Test
    public void testGetWithVideos() {

        final String id = "123";
        TestHelpers.addVideoAndDancer(graphDb, neo4jDancerService, id);
        try(Transaction tx = graphDb.beginTx()){
            final Dancer dancer = this.neo4jDancerService.get(id);
            assertEquals(dancer.getName(), id);
            assertEquals(dancer.getVideos().size(), 1);
            tx.success();
        }
    }

    @Test
    public void testList() throws Exception {
        TestHelpers.addVideoAndDancer(graphDb, neo4jDancerService, "123");
        try (Transaction tx = graphDb.beginTx()) {


            final List<Dancer> list = neo4jDancerService.list();
            assertEquals(1, list.size());
            assertEquals(1, list.get(0).getVideos().size());

            tx.success();
        }

    }


}