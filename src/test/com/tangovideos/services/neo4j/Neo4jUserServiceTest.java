package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.test.TestGraphDatabaseFactory;

import static org.junit.Assert.assertEquals;

public class Neo4jUserServiceTest {

    private GraphDatabaseService graphDb;
    private Neo4jUserService userService;

    @Before
    public void setUp() throws Exception {
        graphDb = new TestGraphDatabaseFactory().newImpermanentDatabase();
        userService = new Neo4jUserService(graphDb);
    }

    @After
    public void tearDown() throws Exception {
        graphDb.shutdown();
    }

    @Test
    public void testGetAllRolesAdmin() throws Exception {
        TestHelpers.setUpAdminNode(graphDb);
        assertEquals(ImmutableSet.of(
                new WildcardPermission("video:read"),
                new WildcardPermission("video:write")
        ), userService.getAllPermissions("admin"));
    }

    @Test
    public void testGetAllRolesNobody() throws Exception {
        TestHelpers.setUpAdminNode(graphDb);
        assertEquals(ImmutableSet.of(), userService.getAllPermissions("nobody"));
    }
}