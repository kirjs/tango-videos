package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.data.Labels;
import com.tangovideos.services.Interfaces.PermissionsService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import java.util.HashSet;
import java.util.stream.Collectors;


public class Neo4jPermissionsService implements PermissionsService {
    private final GraphDatabaseService graphDb;

    public Neo4jPermissionsService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public Node addPermission(String permissionId) {
        final Node permission = this.graphDb.createNode(Labels.PERMISSION.label);
        permission.setProperty("label", permissionId);
        return permission;
    }

    @Override
    public HashSet<Node> addPermissions(ImmutableSet<String> permissions) {
        return permissions
                .stream()
                .map(this::addPermission)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
