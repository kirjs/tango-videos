package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.KeyValue;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

public class Neo4jAdminToolsServiceTest {

    private GraphDatabaseService graphDb;
    private Neo4jAdminToolsService adminService;
    private Neo4jDancerService dancerService;
    private Neo4jSongService songService;


    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        adminService = new Neo4jAdminToolsService(graphDb);
        dancerService = new Neo4jDancerService(graphDb);
        songService = new Neo4jSongService(graphDb);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testRenameDancer() throws Exception {
        final String oldName = "Le g";
        final String betterName = "BetterName";

        TestHelpers.addVideoAndDancer(graphDb, "anyVideo", oldName);
        TestHelpers.addVideoAndDancer(graphDb, "otherVideo", oldName);

        adminService.renameDancer(oldName, betterName);

        String query = "MATCH (v:Video)<-[:DANCES]-(d:Dancer {id: {id}}) return v";

        final ImmutableMap<String, Object> oldDancerParams = ImmutableMap.of("id", oldName);
        final ImmutableMap<String, Object> newDancerParams = ImmutableMap.of("id", betterName);


        try (Transaction tx = graphDb.beginTx()) {
            final Result oldDancers = graphDb.execute(query, oldDancerParams);
            assertEquals(0, IteratorUtil.count(oldDancers));
            final Result newDancers = graphDb.execute(query, newDancerParams);
            assertEquals(2, IteratorUtil.count(newDancers));
            tx.success();
        }
    }

    @Test
    public void testStats() throws Exception {
        TestHelpers.addVideoAndDancer(graphDb, "Video1", "Dancer1");
        TestHelpers.addVideoAndDancer(graphDb, "Video2", "Dancer1");
        TestHelpers.addVideoAndDancer(graphDb, "Video3", "Dancer2");
        dancerService.insertOrGetNode("Inactive dancer");


        final Map<String, String> result = adminService.stats().stream().collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        assertEquals("3", result.get("videos"));
        assertEquals("2", result.get("activeDancers"));
        assertEquals("3", result.get("allDancers"));
        assertEquals("0", result.get("songs"));
    }

    @Test
    public void testEmptyStats() throws Exception {
        final Map<String, String> result = adminService.stats().stream().collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
        assertEquals("0", result.get("videos"));
        assertEquals("0", result.get("activeDancers"));
        assertEquals("0", result.get("allDancers"));
        assertEquals("0", result.get("songs"));
    }
}
