package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Video;
import com.tangovideos.services.YoutubeService;
import org.easymock.EasyMock;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashSet;

import static org.easymock.EasyMock.expect;

public class TestHelpers {
    public static Node setUpAdminNode(GraphDatabaseService graphDb) {
        Node node;
        try (Transaction tx = graphDb.beginTx()) {
            final HashSet<Node> nodes =
                    new Neo4jPermissionsService(graphDb).addPermissions(ImmutableSet.of("video:read", "video:write"));

            final Node adminRole = new Neo4JRoleService(graphDb).addRole("admin", nodes);

            node = new Neo4jUserService(graphDb).addUser("admin", "hashme", adminRole);
            tx.success();
        }
        return node;
    }

    public static void addVideoAndDancer(GraphDatabaseService graphDb, Neo4jDancerService neo4jDancerService, String id) {
        final Neo4jUserService userService  = new Neo4jUserService(graphDb);
        final YoutubeService youtubeService = EasyMock.mock(YoutubeService.class);


        TestHelpers.setUpAdminNode(graphDb);

        final Neo4jVideoService neo4jVideoService = new Neo4jVideoService(
                graphDb,
                userService,
                youtubeService,
                neo4jDancerService
        );
        expect(youtubeService.getVideoInfo("123")).andReturn(new Video("123", "title", "date"));
        EasyMock.replay(youtubeService);
        neo4jVideoService.addVideo("123", TestHelpers.setUpAdminNode(graphDb));
        neo4jVideoService.addDancer("123", id);
    }


}
