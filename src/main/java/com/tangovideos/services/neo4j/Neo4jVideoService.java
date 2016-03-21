package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.tangovideos.data.Labels;
import com.tangovideos.models.Event;
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

import static com.google.common.collect.ImmutableMap.of;


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

        video.setDescription(node.getProperty("description").toString());
        if (node.hasProperty("recordedAt")) {
            video.setRecordedAt(node.getProperty("recordedAt").toString());
        }
        if (node.hasProperty("type")) {
            video.setType(node.getProperty("type").toString());
        }

        video.setAddedAt(node.getProperty("addedAt").toString());
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

        return getMultipleVideos(query, of());
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

        return getMultipleVideos(query, of("skip", skip, "limit", limit));
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
            graphDb.execute(query, of("id", id));
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


        return getMultipleVideos(query, of("dancerId", dancerId));
    }

    @Override
    public Set<String> exist(Set<String> ids) {
        final ImmutableMap<String, Object> params = of("ids", ImmutableList.copyOf(ids));
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


        queryParams = "WHERE (" +
                "v.type = 'Dance performance' OR " +
                "v.type = '' OR " +
                "NOT exists(v.type))  "
                + (queryParams.isEmpty() ? "" : " AND (" + queryParams + ") ");


        final String query = "MATCH (v:Video) " +
                "OPTIONAL MATCH (d:Dancer)-[:DANCES]->(v) " +
                "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                "WITH d, v, s " +
                queryParams +
                "RETURN v, collect(d.id) as dancers, collect(s) as songs";

        return getMultipleVideos(query, of());
    }


    final private Set<String> allowedParameters = ImmutableSet.of("recordedAt", "type");

    @Override
    public void updateField(String id, String field, String value) {
        if (!allowedParameters.contains(field)) {
            throw new NoSuchElementException(String.format("Invalid parameter: %s", field));
        }
        String query = String.format("MATCH (v:Video {id: {id}}) " +
                "SET v.%s = {value} " +
                "RETURN v", field);

        final ImmutableMap<String, Object> params = of(
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
    public void updateEvent(String id, String eventName, String eventInstance) {
        final String query =
                // Match the video
                "MATCH (v:Video {id: {id}}) " +
                        // Get existing event, and remove the previous relationship
                        "OPTIONAL MATCH (v)-[r:HAPPENED_AT]->(e:Event) " +
                        "WHERE e.id <> {name} " +
                        "DELETE r " +
                        "WITH v " +
                        // Merge event instance
                        "MERGE (e:Event {id: {name}}) " +
                        "MERGE (v)-[r:HAPPENED_AT]->(e) " +
                        "SET r.id = {instance}" +
                        "RETURN e, v";

        final ImmutableMap<String, Object> params = of("id", id, "name", eventName, "instance", eventInstance);

        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query, params)) {
            tx.success();
        }

    }


    @Override
    public Video getVideo(String id) {
        final String query =
                "MATCH (v:Video {id: {id}}) " +
                        "OPTIONAL MATCH (v)<-[:DANCES]-(d:Dancer) " +
                        "OPTIONAL MATCH (v)<-[:PLAYS_IN]-(s:Song) " +
                        "OPTIONAL MATCH (v)-[i:HAPPENED_AT]->(e)" +
                        "WHERE v.id = {id}" +
                        "RETURN v, collect(d.id) as dancers, collect(s) as songs, i.id as instance, e.id as event";

        return getMultipleVideos(query, of("id", id)).get(0);
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

                Event event = new Event();


                final Object eventName = next.get("event");
                event.setName(eventName == null ? "" : eventName.toString());
                final Object eventInstance = next.get("instance");
                event.setInstance(eventInstance == null ? "" : eventInstance.toString());
                video.setEvent(event);

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
