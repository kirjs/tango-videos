package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.ResultWithCount;
import com.tangovideos.models.Song;
import com.tangovideos.services.Interfaces.SongService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;
import org.neo4j.helpers.collection.IteratorUtil;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableMap.of;

public class Neo4jSongService implements SongService {
    final private GraphDatabaseService graphDb;
    final private Set<String> allowedParameters = ImmutableSet.of("year", "name", "orquestra", "genre");


    public Neo4jSongService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public Song updateField(String videoId, Integer index, String field, String value) {
        if (!allowedParameters.contains(field)) {
            throw new NoSuchElementException(String.format("Invalid parameter: %s", field));
        }
        Song song;
        try (Transaction tx = graphDb.beginTx()) {
            String query = String.format("MATCH (v:Video {id: {videoId}}) " +
                    "MERGE v<-[:PLAYS_IN{index: {index}}]-(s:Song) " +
                    "SET s.%s = {value} " +
                    "RETURN s as song", field);

            final ImmutableMap<String, Object> parameters = of(
                    "videoId", videoId,
                    "index", index,
                    "value", value);

            final Result result = graphDb.execute(query, parameters);
            song = mapNode((Node) result.next().get("song"));
            result.close();
            tx.success();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return song;
    }

    @Override
    public Song getSong(String videoId, Integer index) {
        String query = "MATCH (song:Song)-[:PLAYS_IN{index: {index}}]->(v:Video {id: {videoId}}) " +
                "RETURN song";
        final ImmutableMap<String, Object> parameters = of("videoId", videoId, "index", index);

        Song song;
        try (Transaction transaction = graphDb.beginTx()) {
            final Result result = graphDb.execute(query, parameters);
            if (result.hasNext()) {
                song = mapNode((Node) result.next().get("song"));
            } else {
                throw new NoSuchElementException();
            }

            result.close();
            transaction.success();
        }
        return song;

    }

    @Override
    public List<Song> list() {
        final String query = "MATCH (s:Song) " +
                "WITH distinct(s.name) as song " +
                "MATCH (s:Song {name:song}) " +
                "WITH song, count(s) as count " +
                "MATCH (s:Song {name:song}) " +
                "RETURN s, count " +
                "ORDER BY count DESC";


        try (Transaction tx = graphDb.beginTx(); Result result = graphDb.execute(query)) {
            tx.success();
            return IteratorUtil.asList(result.<Node>columnAs("s"))
                    .stream()
                    .map(Neo4jSongService::mapNode)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public List<String> listNames() {
        return listByField("name")
                .stream()
                .map(ResultWithCount::getResult)
                .collect(Collectors.toList());
    }

    @Override
    public List<ResultWithCount<String>> listOrquestras() {
        return listByField("orquestra");
    }

    private List<ResultWithCount<String>> listByField(String field) {
        final String query = String.format(
                "MATCH (s:Song) " +
                        "WHERE exists(s.%s) " +
                        "RETURN s.%s as result, count(s.%s) as count", field, field, field);
        try (
                final Transaction tx = graphDb.beginTx();
                final Result result = graphDb.execute(query)
        ) {
            tx.success();
            return IteratorUtil.asList(result)
                    .stream()
                    .map(r -> new ResultWithCount<>(
                            r.get("result").toString(),
                            (Long) r.get("count")
                    ))
                    .collect(Collectors.toList());

        }

    }

    public static Song mapNode(Node songNode) {
        final Song song = new Song();
        if (songNode.hasProperty("year")) {
            song.setYear(songNode.getProperty("year").toString());
        }
        if (songNode.hasProperty("name")) {
            song.setName(songNode.getProperty("name").toString());
        }
        if (songNode.hasProperty("orquestra")) {
            song.setOrquestra(songNode.getProperty("orquestra").toString());
        }
        if (songNode.hasProperty("genre")) {
            song.setGenre(songNode.getProperty("genre").toString());
        }
        return song;
    }
}
