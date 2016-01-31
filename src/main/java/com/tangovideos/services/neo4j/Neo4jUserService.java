package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.services.Interfaces.UserService;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.Map;
import java.util.Set;


public class Neo4jUserService implements UserService {


    private final GraphDatabaseService graphDb;


    public Neo4jUserService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;


        fillTheDb();

    }

    private void fillTheDb() {
        try (Transaction tx = this.graphDb.beginTx()) {
            final Node admin = this.graphDb.createNode(Labels.USER.label);
            admin.setProperty("password", "hashme");
            admin.setProperty("id", "admin");

            final Node adminRole = this.graphDb.createNode(Labels.ROLE.label);
            adminRole.setProperty("label", "role");
            admin.createRelationshipTo(adminRole, Relationships.IS);

            final Node writeVideosPermission = this.graphDb.createNode(Labels.PERMISSION.label);
            writeVideosPermission.setProperty("label", "videos:write");
            adminRole.createRelationshipTo(writeVideosPermission, Relationships.CAN);

            final Node readVideosPermission = this.graphDb.createNode(Labels.PERMISSION.label);
            readVideosPermission.setProperty("label", "videos:read");
            adminRole.createRelationshipTo(readVideosPermission, Relationships.CAN);

            tx.success();
        }
    }


    @Override
    public boolean userExists(String id) {
        int count;
        try (Transaction tx = this.graphDb.beginTx()) {
            final ResourceIterator<Node> nodes = graphDb.findNodes(Labels.USER.label, "id", id);
            count = IteratorUtil.count(nodes);
            nodes.close();
            tx.success();
        }
        return count > 0;
    }

    @Override
    public boolean verifyCredentials(String id, String password) {

        try (Transaction tx = this.graphDb.beginTx()) {
            final ResourceIterator<Node> nodes = graphDb.findNodes(Labels.USER.label, "id", id);
            if (nodes.hasNext()) {
                final Node next = nodes.next();
                final Map<String, Object> allProperties = next.getAllProperties();
                final Object userPassword = next.getProperty("password");
                if (userPassword.equals(password)) {
                    return true;
                }
            }
            tx.success();
        }

        return false;
    }

    @Override
    public void list() {

    }

    @Override
    public Set<Permission> getAllRoles(String id) {
        final String query = "MATCH (u:User)-[:IS]->(r:Role)-[:CAN]->(p:Permission) " +
                "WHERE u.id = \"admin\" " +
                "RETURN p.label";
        final ImmutableMap<String, Object> params = ImmutableMap.<String, Object>of("id", id);
        Set<Permission> result = Sets.newHashSet();
        final Result results = this.graphDb.execute(query, params);
        while(results.hasNext()){
            result.add(new WildcardPermission(results.next().get("p.label").toString()));
        }

        return result;
    }
}
