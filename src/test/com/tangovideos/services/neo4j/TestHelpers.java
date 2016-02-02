package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
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


}
