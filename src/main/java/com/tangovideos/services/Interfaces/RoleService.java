package com.tangovideos.services.Interfaces;
import org.neo4j.graphdb.Node;

import java.util.HashSet;

public interface RoleService {
    Node addRole(String videoId, HashSet<Node> nodes);

    void addPermissions(Node adminRole, HashSet<Node> nodes);
}
