package com.tangovideos.resources;

import com.tangovideos.resources.inputs.JustName;
import com.tangovideos.services.Interfaces.AdminToolsService;
import com.tangovideos.services.neo4j.Neo4jAdminToolsService;
import com.tangovideos.services.neo4j.Neo4jDancerService;
import com.tangovideos.services.neo4j.Neo4jVideoService;
import com.tangovideos.services.neo4j.TestHelpers;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

public class DancerResourceTest {
    private GraphDatabaseService graphDb;
    private DancerResource dancerResource;


    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        Neo4jDancerService dancerService = new Neo4jDancerService(graphDb);
        Neo4jVideoService videoService = new Neo4jVideoService(graphDb);
        AdminToolsService adminToolsService = new Neo4jAdminToolsService(graphDb);
        dancerResource = new DancerResource(dancerService, videoService, adminToolsService);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    public int countVideosForDancer(String dancerId) {
        final JSONObject result = new JSONObject(dancerResource.getVideo(dancerId).getEntity().toString());
        return result.getJSONArray("videos").length();
    }

    public int renameAndCountPseudonyms(String dancerId, String dancerId2) {
        final Response response = dancerResource
                .addPseudonym(dancerId, JustName.fromName(dancerId2));
        final JSONArray array = new JSONArray(response.getEntity().toString());
        return array.length();
    }

    @Test
    public void testAddingPseudonyms() {
        final String dancerId = "dancer0";
        final String dancerId1 = "dancer1";
        final String dancerId2 = "dancer2";

        TestHelpers.addVideoAndDancer(graphDb, "video-1", dancerId);
        TestHelpers.addVideoAndDancer(graphDb, "video-2", dancerId1);
        TestHelpers.addVideoAndDancer(graphDb, "video-3", dancerId2);

        // When adding pseudonyme which is exactly the same as the name nothing happens.
        assertEquals(1, countVideosForDancer(dancerId));
        assertEquals(0, renameAndCountPseudonyms(dancerId, dancerId));
        assertEquals(1, countVideosForDancer(dancerId));


        // When adding another pseudonyme, all videos from it migrate to the first one
        assertEquals(1, renameAndCountPseudonyms(dancerId, dancerId1));
        assertEquals(countVideosForDancer(dancerId), 2);

        // When adding another pseudonyme, all videos from it migrate to the first one
        assertEquals(2, renameAndCountPseudonyms(dancerId2, dancerId));
        assertEquals(countVideosForDancer(dancerId2), 3);



//        // Dancers can be renamed back
//        dancerResource.addPseudonym(dancerId, JustName.fromName(dancerId1));
//        assertEquals(countVideosForDancer(dancerId), 2);
//
//        // Dancers can be renamed back
//        dancerResource.addPseudonym(dancerId2, JustName.fromName(dancerId));
//        assertEquals(countVideosForDancer(dancerId2), 2);
    }
}