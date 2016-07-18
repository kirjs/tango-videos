package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Dancer;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.DancerService;
import org.neo4j.graphdb.*;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;


public class Neo4jDancerService implements DancerService {
    private final GraphDatabaseService graphDb;

    public enum SortBy {
        VIDEO_COUNT
    }

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
        List<Dancer> dancers;

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "RETURN DISTINCT d as dancer";
            final ResourceIterator<Node> result = this.graphDb.execute(query).columnAs("dancer");
            dancers = IteratorUtil.asList(result).stream().map(this::mapNode).collect(Collectors.toList());
            tx.success();
        }

        return dancers;
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Dancer> list(int skip, int limit, SortBy sortBy) {
        List<Dancer> dancers = Lists.newArrayList();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "RETURN DISTINCT d as dancer, collect(v) as videos,  count(v.id) as count " +
                    "ORDER BY count DESC";
            of("skip", skip, "limit", limit);
            final Result result = this.graphDb.execute(query);


            while (result.hasNext()) {
                final Map<String, Object> next = result.next();
                final Dancer dancer = this.mapNode((Node) next.get("dancer"));
                final List<Node> videos = IteratorUtil.asList((Iterable<Node>) next.get("videos"));
                final List<Video> collect = videos.stream()
                        .map(Neo4jVideoService::mapNodeToVideo).limit(3).collect(Collectors.toList());
                dancer.setVideos(collect);
                dancer.setVideoCount(videos.size());
                dancers.add(dancer);
            }

            tx.success();
        }

        return dancers;
    }


    @Override
    public void addToVideo(Node dancer, Node video) {
        String query = "MATCH (d:Dancer {id: {dancerId}}) " +
                "MATCH (v:Video {id: {videoId}}) " +
                "MERGE (d)-[r:DANCES]->(v) " +
                "RETURN d, v,r";

        this.addOrRemoveDancer(query, dancer, video);
    }

    @Override
    public Set<Set<String>> getaAllDancersByVideo() {
        final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                "WITH v, collect(d) as dancers " +
                "RETURN DISTINCT dancers";


        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
            tx.success();

            return IteratorUtil.<Iterable<Node>>asSet(result.columnAs("dancers"))
                    .stream()
                    .map(IteratorUtil::asSet)
                    .map(s -> s.stream()
                            .map(n -> n.getProperty("id").toString()).collect(Collectors.toSet()))
                    .collect(Collectors.toSet());
        }
    }


    public void addOrRemoveDancer(String query, Node dancer, Node video) {
        try (Transaction tx = this.graphDb.beginTx()) {
            if (!dancer.hasLabel(Labels.DANCER.label)) {
                throw new RuntimeException("Expected dancer node, got: " + dancer.getLabels());
            }
            final ImmutableMap<String, Object> params = of("videoId", video.getProperty("id"), "dancerId", dancer.getProperty("id"));

            Result result = graphDb.execute(query, params);

            result.close();
            tx.success();
        }
    }

    @Override
    public void removeFromVideo(Node dancer, Node video) {
        String query = "MATCH (d:Dancer {id: {dancerId}})-[r:DANCES]->(v:Video {id: {videoId}}) " +
                "DELETE r " +
                "RETURN d, v,r";

        this.addOrRemoveDancer(query, dancer, video);

    }

    @Override
    public Node insertOrGetNode(String dancerId) {
        Node dancer;

        final String query = "MERGE (d:Dancer {id: {id}}) RETURN d as dancer";
        final ImmutableMap<String, Object> params = of("id", dancerId);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            dancer = result.<Node>columnAs("dancer").next();
            tx.success();
        }

        return dancer;
    }

    @Override
    public Set<String> getForVideo(String videoId) {
        HashSet<String> result = Sets.newHashSet();

        try (Transaction tx = this.graphDb.beginTx()) {
            final String query = "MATCH (d:Dancer)-[:DANCES]->(v:Video) " +
                    "WHERE v.id = {id} " +
                    "RETURN d.id";

            final Result results = this.graphDb.execute(query, of("id", videoId));
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
        final String query = "MATCH (d:Dancer {id:{id}})" +
                "OPTIONAL MATCH (d)-[:IS_ALSO]-(p) " +
                "RETURN d, collect(p.id) as p";
        final ImmutableMap<String, Object> params = of("id", id);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            final Map<String, Object> next = result.next();
            final Dancer dancer = this.mapNode((Node) next.get("d"));

            dancer.setPseudonyms(Sets.newHashSet((Iterable<String>) next.get("p")));
            tx.success();

            return dancer;
        }
    }

    @Override
    public Dancer addPseudonym(String id, String name) {
        final String query =
                "MERGE (d:Dancer {id: {id}}) " +
                "MERGE (p:Pseudonym {id: {name}}) " +
                "MERGE (d)-[:IS_ALSO]->(p) " +
                "WITH d " +
                "OPTIONAL MATCH (:Dancer {id: {name}})-[r:IS_ALSO]->(p:Pseudonym) " +
                "MERGE (d)-[:IS_ALSO]->(p) " +
                "with r, d " +
                "DELETE r " +
                "RETURN d";

        final ImmutableMap<String, Object> params = of("id", id, "name", name);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            tx.success();
            return mapNode(result.<Node>columnAs("d").next());
        }
    }

    @Override
    public Dancer removePseudonym(String id, String name) {
        final String query = "MATCH (d:Dancer {id: {id}})-[r:IS_ALSO]->(p:Pseudonym {id: {name}}) " +
                "DELETE r " +
                "DELETE p " +
                "RETURN d";
        final ImmutableMap<String, Object> params = of("id", id, "name", name);


        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            tx.success();
            return mapNode(result.<Node>columnAs("d").next());
        }
    }

    @Override
    public Map<String, Set<String>> getPseudonyms() {
        final String query = "MATCH (p:Pseudonym)<-[:IS_ALSO]-(d:Dancer) " +
                "return p.id as name, collect(d.id) as dancers";

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {

            tx.success();
            return IteratorUtil.asList(result).stream()
                    .collect(Collectors.toMap(
                            r -> r.get("name").toString(),
                            r -> Sets.newHashSet((Iterable<String>) r.get("dancers"))
                    ));
        }

    }
}
