package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.tangovideos.models.Dancer;
import com.tangovideos.models.KeyValue;
import com.tangovideos.services.Interfaces.VideoService;
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

import static com.google.common.collect.ImmutableMap.of;
import static org.junit.Assert.assertEquals;

public class Neo4jAdminToolsServiceTest {

    private GraphDatabaseService graphDb;
    private Neo4jAdminToolsService adminService;
    private Neo4jDancerService dancerService;
    private Neo4jSongService songService;
    private VideoService videoService;


    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        adminService = new Neo4jAdminToolsService(graphDb);
        dancerService = new Neo4jDancerService(graphDb);
        songService = new Neo4jSongService(graphDb);
        videoService = new Neo4jVideoService(graphDb);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    // Some egdecase where dancers who were tagged on removed videos failed to get renamed
    public void testRenameDancerWithRemovedVideos() throws Exception {
        final String videoid = "videoid";
        final String dancerId = "dancerId";
        final String otherId = "otherId";

        TestHelpers.addVideoAndDancer(graphDb, videoid, dancerId);
        TestHelpers.addVideoAndDancer(graphDb, "OtherVideo", dancerId);

        videoService.hide(videoid, true);
        adminService.renameDancer("dancerId", otherId);
        final Dancer dancer = dancerService.get(otherId);
        assertEquals(otherId, dancer.getName());

    }

    @Test
    public void testRenameDancer() throws Exception {
        final String oldName = "Le g";
        final String betterName = "BetterName";


        String dancersQuery = "MATCH (d:Dancer {id: {id}}) return d";
        String videosWithDancersQuery = "MATCH (v:Video)<-[:DANCES]-(d:Dancer {id: {id}}) return v";

        final ImmutableMap<String, Object> oldDancerParams = of("id", oldName);
        final ImmutableMap<String, Object> newDancerParams = of("id", betterName);


        try (Transaction tx = graphDb.beginTx()) {

            TestHelpers.addVideoAndDancer(graphDb, "anyVideo", oldName);
            TestHelpers.addVideoAndDancer(graphDb, "otherVideo", oldName);

            // Assert there is just one dancer node initially connected with 2 videos
            final Result initialOldDancers = graphDb.execute(dancersQuery, oldDancerParams);
            assertEquals(1, IteratorUtil.count(initialOldDancers));

            // Assert that renaming the dancer return number of videos
            assertEquals(2, adminService.renameDancer(oldName, betterName));


            // Assert that there are 0 videos for old and 2 for new dancers
            final Result oldDancers = graphDb.execute(videosWithDancersQuery, oldDancerParams);
            assertEquals(0, IteratorUtil.count(oldDancers));
            final Result newDancers = graphDb.execute(videosWithDancersQuery, newDancerParams);
            assertEquals(2, IteratorUtil.count(newDancers));

            // Assert there is just one new  dancer node eventually
            final Result eventuallyOldDancers = graphDb.execute(dancersQuery, newDancerParams);
            assertEquals(1, IteratorUtil.count(eventuallyOldDancers));

            // Assert that the old node was removed
            final Result eventuallylOldDancers = graphDb.execute(dancersQuery, oldDancerParams);
            assertEquals(0, IteratorUtil.count(eventuallylOldDancers));
            tx.success();
        }
    }

    @Test
    public void testStats() throws Exception {
        try (Transaction tx = graphDb.beginTx()) {
            TestHelpers.addVideoAndDancer(graphDb, "Video1", "Dancer1");
            TestHelpers.addVideoAndDancer(graphDb, "Video2", "Dancer1");
            TestHelpers.addVideoAndDancer(graphDb, "Video3", "Dancer2");
            dancerService.insertOrGetNode("Inactive dancer");


            final Map<String, String> result = adminService.stats().stream().collect(Collectors.toMap(KeyValue::getKey, KeyValue::getValue));
            assertEquals("3", result.get("videos"));
            assertEquals("2", result.get("activeDancers"));
            assertEquals("3", result.get("allDancers"));
            assertEquals("0", result.get("songs"));

            tx.success();
        }

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
