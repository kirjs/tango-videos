package com.tangovideos.services.neo4j;

import com.tangovideos.data.Labels;
import com.tangovideos.data.Relationships;
import com.tangovideos.services.Interfaces.RoleService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.HashSet;


public class Neo4JRoleService implements RoleService {
    private final GraphDatabaseService graphDb;

    public Neo4JRoleService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public Node addRole(String roleId, HashSet<Node> nodes) {
        final Node adminRole = this.graphDb.createNode(Labels.ROLE.label);
        adminRole.setProperty("label", roleId);
        addPermissions(adminRole, nodes);
        return adminRole;
    }

    public Node addPermission(Node role, Node permission) {
        role.createRelationshipTo(permission, Relationships.CAN);
        return permission;
    }

    @Override
    public void addPermissions(Node role, HashSet<Node> permissions) {
        permissions.stream().forEach(permission -> this.addPermission(role, permission));

    }
}
