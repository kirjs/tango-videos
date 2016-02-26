package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Channel;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class Neo4jChannelServiceTest {


    private GraphDatabaseService graphDb;
    private Neo4jChannelService neo4jChannelService;


    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        neo4jChannelService = new Neo4jChannelService(graphDb);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testAddChannel() throws Exception {
        String id = "superChannel";

        neo4jChannelService.addChannel(id);

        final String query = "MATCH (c:Channel) RETURN c.id as id";
        final ImmutableMap<String, Object> params = ImmutableMap.of("id", id);
        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            assertEquals(result.next().get("id"), id);
            tx.success();
        }

    }

    @Test
    public void testList() throws Exception {
        neo4jChannelService.addChannel("one");
        neo4jChannelService.addChannel("two");

        final List<Channel> list = neo4jChannelService.list();
        assertEquals(list.size(), 2);
        assertEquals(list.get(0).getId(), "one");
    }
}