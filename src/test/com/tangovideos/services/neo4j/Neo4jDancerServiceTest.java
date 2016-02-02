package com.tangovideos.services.neo4j;

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

import static org.junit.Assert.*;

public class Neo4jDancerServiceTest {

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

    }

    @Test
    public void testGet() {
        try(Transaction tx = this.graphDb.beginTx()){
            this.neo4jDancerService.insertOrGetNode("one");

            final Dancer dancer = this.neo4jDancerService.get("one");
            assertEquals(dancer.getName(), "one");
        }
    }

    @Test
    public void testList() throws Exception {
        try (Transaction tx = this.graphDb.beginTx()) {
            this.neo4jDancerService.insertOrGetNode("one");
            this.neo4jDancerService.insertOrGetNode("two");
            assertEquals(2, this.neo4jDancerService.list().size());
        }

    }
}