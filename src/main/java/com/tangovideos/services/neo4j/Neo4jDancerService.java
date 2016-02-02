package com.tangovideos.services.neo4j;

import com.google.api.client.util.Sets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Dancer;
import com.tangovideos.models.VideoResponse;
import com.tangovideos.services.Interfaces.DancerService;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Neo4jDancerService implements DancerService {
    private final GraphDatabaseService graphDb;
    private Label label = Labels.DANCER.label;

    public Neo4jDancerService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;


    }

    public Dancer mapNode(Node node) {
        final Dancer dancer = new Dancer();
        dancer.setName(node.getProperty("id").toString());
        return dancer;
    }

    @Override
    public List<Dancer> list() {
        final List<Dancer> nodes = Lists.newArrayList();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "RETURN d, collect(v) as videos";

            final Result result = graphDb.execute(query);
            while(result.hasNext()){
                final Map<String, Object> next = result.next();
                final Dancer dancer = this.mapNode((Node) next.get("d"));

                final Iterable<Node> video = (Iterable<Node>) next.get("videos");
                final List<VideoResponse> videos = IteratorUtil.asList(video).stream()
                        .map(Neo4jVideoService::videoResponseFromNode)
                        .collect(Collectors.toList());

                dancer.setVideos(videos);

                nodes.add(dancer);
            }

            tx.success();
        }
        return nodes;
    }

    @Override
    public Node insertOrGetNode(String dancerId) {
        Node dancer;

        try (Transaction tx = this.graphDb.beginTx()) {
            final ResourceIterator<Node> nodes = this.graphDb.findNodes(Labels.DANCER.label, "id", dancerId);
            if (nodes.hasNext()) {
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
            while (results.hasNext()) {
                result.add(results.next().get("d.id").toString());
            }

            results.close();
            tx.success();
        }

        return result;
    }

    @Override
    public Dancer get(String id) {
        Dancer dancer;
        try(final Transaction tx = this.graphDb.beginTx()){
            dancer = this.mapNode(this.graphDb.findNode(this.label, "id", id));
            tx.success();
        }
        return dancer;
    }

}
