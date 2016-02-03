package com.tangovideos.services.neo4j;

import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.YoutubeService;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

public class Neo4jVideoServiceTest  extends EasyMockSupport {

    private GraphDatabaseService graphDb;
    private Neo4jVideoService neo4jVideoService;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        YoutubeService youtubeService = mock(YoutubeService.class);
        DancerService dancerService = mock(DancerService.class);
        neo4jVideoService = new Neo4jVideoService(graphDb);


    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }


    @Test
    public void testVideoExists() throws Exception {

    }

    @Test
    public void testAddVideo() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testVideoResponseFromNode() throws Exception {

    }

    @Test
    public void testList() throws Exception {

    }

    @Test
    public void testAddDancer() throws Exception {

    }
}