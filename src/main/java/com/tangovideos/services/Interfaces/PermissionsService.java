package com.tangovideos.services.Interfaces;

import com.google.common.collect.ImmutableSet;
import org.neo4j.graphdb.Node;

import java.util.HashSet;

public interface PermissionsService {
    Node addPermission(String videoId);

    HashSet<Node> addPermissions(ImmutableSet<String> of);
}
