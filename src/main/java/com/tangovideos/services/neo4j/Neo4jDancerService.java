package com.tangovideos.services.neo4j;

import com.google.api.client.util.Sets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.data.Labels;
import com.tangovideos.services.Interfaces.DancerService;
import org.neo4j.graphdb.*;

import java.util.HashSet;
import java.util.Set;


public class Neo4jDancerService implements DancerService {
    private final GraphDatabaseService graphDb;

    public Neo4jDancerService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;


    }

    @Override
    public Node insertOrGet(String dancerId) {
        Node dancer = null;

        try (Transaction tx = this.graphDb.beginTx()) {
            final ResourceIterator<Node> nodes = this.graphDb.findNodes(Labels.DANCER.label, "id", dancerId);
            if(nodes.hasNext()){
                dancer = nodes.next();
            } else {
                dancer = this.graphDb.createNode(Labels.DANCER.label);
                dancer.setProperty("id", dancerId);
            }
            nodes.close();
            tx.success();
        }
        return dancer;
    }

    @Override
    public Set<String> getForVideo(String videoId) {
        HashSet<String> result = Sets.newHashSet();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (v:Video)<-[:DANCES]-(d:Dancer) " +
                    "WHERE v.id = {id} " +
                    "RETURN d.id";

            final Result results = this.graphDb.execute(query, ImmutableMap.of("id", videoId));
            while(results.hasNext()){
                result.add(results.next().get("d.id").toString());
            }

            results.close();
            tx.success();
        }

        return result;
    }

}
