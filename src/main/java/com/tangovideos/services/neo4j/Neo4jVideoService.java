package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Song;
import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.VideoService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;


public class Neo4jVideoService implements VideoService {
    private final GraphDatabaseService graphDb;

    public Neo4jVideoService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public boolean exists(String videoId) {
        try (Transaction tx = graphDb.beginTx()) {
            final boolean result = this.graphDb.findNodes(Labels.VIDEO.label, "id", videoId).hasNext();
            tx.success();
            return result;
        }
    }

    @Override
    public Node addVideo(@NotNull Video video) {
        final Node node;
        try (Transaction tx = graphDb.beginTx()) {
            if (exists(video.getId())) {
                throw new RuntimeException("VideoExists");
            }

            node = graphDb.createNode(Labels.VIDEO.label);
            node.setProperty("title", video.getTitle());
            node.setProperty("description", video.getDescription());
            node.setProperty("publishedAt", video.getPublishedAt());
            node.setProperty("recordedAt", video.getPublishedAt());
            node.setProperty("addedAt", Instant.now().getEpochSecond());
            node.setProperty("id", video.getId());
            tx.success();
        }
        return node;
    }

    @Override
    public Node get(String videoId) {
        Node video;
        try (Transaction tx = this.graphDb.beginTx()) {
            video = this.graphDb.findNode(Labels.VIDEO.label, "id", videoId);
            tx.success();
        }
        return video;
    }

    public static Video mapNodeToVideo(Node node) {
        Video video = new Video(
                node.getProperty("id").toString(),
                node.getProperty("title").toString(),
                node.getProperty("publishedAt").toString()
        );

        if (node.hasProperty("recordedAt")) {
            video.setRecordedAt(node.getProperty("recordedAt").toString());
        }
        video.setAddedAt(node.getProperty("addedAt").toString());
        video.setComplete(node.hasProperty("complete") && node.getProperty("complete").toString().equals("true"));
        return video;
    }

    @Override
    public List<Video> list() {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs " +
                        "ORDER BY v.addedAt DESC";

        return getMultipleVideos(query, ImmutableMap.of());
    }

    @Override
    public List<Video> list(int skip, int limit) {
        final String query =
                "MATCH (v:Video) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs " +
                        "ORDER BY v.addedAt DESC " +
                        "SKIP {skip} " +
                        "LIMIT {limit}";

        return getMultipleVideos(query, ImmutableMap.of("skip", skip, "limit", limit));
    }

    @Override
    public boolean hide(String id, Boolean value) {
        String query;
        if (value) {
            query = "MATCH (v:Video {id: {id}}) " +
                    "SET v:VideoRemoved " +
                    "REMOVE v:Video " +
                    "RETURN v ";
        } else {
            query = "MATCH (v:VideoRemoved  {id: {id}}) " +
                    "SET v:Video " +
                    "REMOVE v:VideoRemoved " +
                    "RETURN v ";
        }

        try (Transaction tx = graphDb.beginTx()) {
            graphDb.execute(query, ImmutableMap.of("id", id));
            tx.success();
            return true;
        }
    }

    @Override
    public List<Video> listByDancer(String dancerId) {
        final String query =
                "MATCH (v:Video) " +
                        "MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "WHERE d.id = {dancerId} " +
                        "WITH v as v " +
                        "MATCH (d:Dancer)-[:DANCES]->(v) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs";


        return getMultipleVideos(query, ImmutableMap.of("dancerId", dancerId));
    }

    @Override
    public Set<String> exist(Set<String> ids) {
        final ImmutableMap<String, Object> params = ImmutableMap.of("ids", ImmutableList.copyOf(ids));
        final String query = "MATCH (v:Video) WHERE v.id IN {ids} RETURN v.id as id";
        final Result result = graphDb.execute(query, params);
        return IteratorUtil.asSet(result.columnAs("id"));
    }

    private ImmutableMap<Object, Object> paramQueries =
            ImmutableMap.builder()
                    .put("dancers", "NOT (:Dancer)-[:DANCES]->(v)")
                    .put("songname", "NOT (:Song)-[:PLAYS_IN]->(v) OR NOT exists(s.name)")
                    .put("orquestra", "NOT (:Song)-[:PLAYS_IN]->(v) OR NOT exists(s.orquestra)")
                    .put("genre", "NOT (:Song)-[:PLAYS_IN]->(v) OR NOT exists(s.genre)")
                    .put("year", "NOT (:Song)-[:PLAYS_IN]->(v) OR NOT exists(s.year)")
                    .build();
    ;


    /**
     * Find videos that don't have any dancers assigned.
     *
     * @return list of videos
     */
    @Override
    public List<Video> needsReview(Map<String, Boolean> params) {

        String queryParams = params.entrySet()
                .stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .filter(paramQueries::containsKey)
                .map(paramQueries::get)
                .map(Object::toString)
                .collect(Collectors.joining(" OR "));

        if (queryParams.length() > 0) {
            queryParams = "WHERE " + queryParams + " ";
        }


        final String query = "MATCH (v:Video) " +
                "OPTIONAL MATCH (d:Dancer)-[:DANCES]->(v) " +
                "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                "WITH d, v, s " +
                queryParams +
                "RETURN v, collect(d.id) as dancers, collect(s) as songs";

        return getMultipleVideos(query, ImmutableMap.of());
    }


    final private Set<String> allowedParameters = ImmutableSet.of("recordedAt");

    @Override
    public void updateField(String id, String field, String value) {
        if (!allowedParameters.contains(field)) {
            throw new NoSuchElementException(String.format("Invalid parameter: %s", field));
        }
        String query = String.format("MATCH (v:Video {id: {id}}) " +
                "SET v.%s = {value} " +
                "RETURN v", field);

        final ImmutableMap<String, Object> params = ImmutableMap.of(
                "id", id,
                "value", value);


        try (
                Transaction tx = graphDb.beginTx();
                final Result result = graphDb.execute(query, params)
        ) {
            tx.success();
        }

    }

    @Override
    public void markComplete(String id, Boolean value) {
        final String query = "MATCH (v:Video {id: {id}}) " +
                "SET v.complete = {value} " +
                "RETURN v ";

        final ImmutableMap<String, Object> params = ImmutableMap.of("id", id, "value", value);
        try (
                Transaction tx = graphDb.beginTx();
                Result result = graphDb.execute(query, params)
        ) {
            tx.success();
        }
    }

    @SuppressWarnings("unchecked")
    private List<Video> getMultipleVideos(String query, Map<String, Object> params) {
        List<Video> videos = Lists.newArrayList();
        try (Transaction tx = this.graphDb.beginTx()) {
            final Result result = this.graphDb.execute(query, params);

            while (result.hasNext()) {
                final Map<String, Object> next = result.next();
                final Node videoNode = (Node) next.get("v");
                final Video video = mapNodeToVideo(videoNode);
                video.setDancers(Sets.newHashSet((Iterable<String>) next.get("dancers")));
                final List<Song> songs = Sets.newHashSet((Iterable<Node>) next.get("songs"))
                        .stream()
                        .map(Neo4jSongService::mapNode)
                        .collect(Collectors.toList());
                video.setSongs(songs);

                videos.add(video);
            }
            tx.success();
        }
        return videos;
    }


}
