package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Video;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashSet;

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

    public static void addVideoAndDancer(GraphDatabaseService graphDb, String videoId, String dancerId) {
        final Neo4jDancerService neo4jDancerService = new Neo4jDancerService(graphDb);
        final Node video = new Neo4jVideoService(graphDb).addVideo(new Video(videoId, "Title", "Date"));


        neo4jDancerService.addToVideo(
                neo4jDancerService.insertOrGetNode(dancerId),
                video
        );
    }

    public static void addDancer() {

    }


}
