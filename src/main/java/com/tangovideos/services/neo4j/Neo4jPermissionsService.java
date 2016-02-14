package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.services.Interfaces.PermissionsService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.HashSet;
import java.util.stream.Collectors;


public class Neo4jPermissionsService implements PermissionsService {
    private final GraphDatabaseService graphDb;

    public Neo4jPermissionsService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public Node addPermission(String label) {
        Node permission;
        String query = "MERGE (p:Permission {label: {label}}) return p";
        final ImmutableMap<String, Object> params = ImmutableMap.of("label", label);
        try(Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params);){
            permission = (Node) result.next().get("p");
            tx.success();
        }
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
