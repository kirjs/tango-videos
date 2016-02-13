package com.tangovideos.services.neo4j;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.junit.Assert.assertEquals;

public class Neo4jSongServiceTest {

    private GraphDatabaseService graphDb;
    private Neo4jSongService neo4jSongService;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        neo4jSongService = new Neo4jSongService(graphDb);

    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testInsert() throws Exception {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);
        assertEquals(neo4jSongService.updateField(videoId, 0, "year", "1934"), null);
    }
}