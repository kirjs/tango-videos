package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.test.TestGraphDatabaseFactory;

import java.util.Map;
import java.util.NoSuchElementException;

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


    @Test(expected=NoSuchElementException.class)
    public void testUpdateFieldUnexistingParameter() {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);

        neo4jSongService.updateField(videoId, 0, "leg", "123");
    }

    @Test
    public void testUpdateFieldName() throws Exception {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);
        final String value = "Poema";
        final int index = 0;
        final String name = "name";
        Song song = neo4jSongService.updateField(videoId, index, name, value);
        assertEquals(null, song.getYear());
        assertEquals(value, song.getName());
        final String year = "1936";
        song = neo4jSongService.updateField(videoId, index, "year", year);
        assertEquals(year, song.getYear());
        assertEquals(value, song.getName());


        try (Transaction tx = graphDb.beginTx()){
            final Result result = graphDb.execute(
                    "MATCH (s:Song)-[:PLAYS_IN{index: {index}}]-(v:Video {id: {videoId}}) " +
                            "RETURN s.name as name, s.year as year",
                    ImmutableMap.of(
                            "videoId", videoId,
                            "index", index
                    ));

            final Map<String, Object> node = result.next();
            assertEquals(value, node.get(name));
            assertEquals(year, node.get("year"));
            tx.success();
        }
    }

    @Test
    public void testUpdateField() throws Exception {

    }

    @Test
    public void testGetSong() throws Exception {

    }



    @Test
    public void testListOrquestras() throws Exception {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);
        final String orquestra1 = "Francisco Canaro";
        neo4jSongService.updateField(videoId, 0, "orquestra", orquestra1);
        final String orquestra2  = "Juan D'Arienzo";
        neo4jSongService.updateField(videoId, 1, "orquestra", orquestra2 );
        final String videoId2 = "test2";
        TestHelpers.addVideo(graphDb, videoId2);
        final String orquestra3 = "Hedgehog";
        neo4jSongService.updateField(videoId2, 0, "orquestra", orquestra3);
        neo4jSongService.updateField(videoId2, 1, "name", "some name");
        final ImmutableSet<String> result = ImmutableSet.copyOf(neo4jSongService.listOrquestras());
        assertEquals(ImmutableSet.of(orquestra1, orquestra2, orquestra3), result);
    }

    @Test
    public void testListNames() throws Exception {
        final String videoId = "test";
        TestHelpers.addVideo(graphDb, videoId);
        final String song1 = "Poema";

        neo4jSongService.updateField(videoId, 1, "name", song1);
        final ImmutableSet<String> result = ImmutableSet.copyOf(neo4jSongService.listNames());
        assertEquals(ImmutableSet.of(song1), result);
    }
}