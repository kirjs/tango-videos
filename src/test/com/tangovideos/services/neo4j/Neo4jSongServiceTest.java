package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
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
    public void testUpdateFieldYear() throws Exception {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);
        final String year = "1934";
        final int index = 0;

        neo4jSongService.updateField(videoId, index, "year", year);

        try (Transaction tx = graphDb.beginTx()){
            final Result result = graphDb.execute(
                    "MATCH (s:Song)-[:PLAYS_IN{index: {index}}]-(v:Video {id: {videoId}}) " +
                            "RETURN s.year as year",
                    ImmutableMap.of(
                            "videoId", videoId,
                            "index", index
                    ));
            assertEquals(year, result.next().get("year"));
            tx.success();
        }
    }

    public void testUpdateFieldName() throws Exception {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);
        final String year = "Poema";
        final int index = 0;
        final String name = "name";
        neo4jSongService.updateField(videoId, index, name, year);

        try (Transaction tx = graphDb.beginTx()){
            final Result result = graphDb.execute(
                    "MATCH (s:Song)-[:PLAYS_IN{index: {index}}]-(v:Video {id: {videoId}}) " +
                            "RETURN s.name as name",
                    ImmutableMap.of(
                            "videoId", videoId,
                            "index", index
                    ));
            assertEquals(year, result.next().get(name));
            tx.success();
        }
    }
}