package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.models.UserProfile;
import com.tangovideos.services.Interfaces.UserService;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class Neo4jUserService implements UserService {


    private final GraphDatabaseService graphDb;


    public Neo4jUserService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
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
    public UserProfile getUserProfile(String id){
        UserProfile profile = new UserProfile();
        profile.setId(id);
        final HashSet<String> permissions = this.getAllRoles(id).stream()
                .map(Object::toString)
                .collect(Collectors.toCollection(HashSet::new));

        profile.setPermissions(permissions);
        return profile;
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

    @Override
    public Node addUser(String id, String password, Node role) {
        final Node admin = this.graphDb.createNode(Labels.USER.label);
        admin.createRelationshipTo(role, Relationships.IS);
        admin.setProperty("password", password);
        admin.setProperty("id", id);
        return admin;
    }
}
