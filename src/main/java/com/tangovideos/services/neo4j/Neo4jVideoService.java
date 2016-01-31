package com.tangovideos.services.neo4j;

import com.tangovideos.data.Labels;
import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.Interfaces.VideoService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;

    public Neo4jVideoService(GraphDatabaseService graphDb, UserService userService) {
        this.graphDb = graphDb;
        fillTheDb();
    }


    private void fillTheDb() {
        try (Transaction tx = this.graphDb.beginTx()) {
            tx.success();
        }
    }


    @Override
    public void addVideo(String videoId, String title, String image, Node admin) {
        final Node adminRole = this.graphDb.createNode(Labels.ROLE.label);


    }
}
