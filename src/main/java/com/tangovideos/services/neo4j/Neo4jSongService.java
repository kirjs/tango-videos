package com.tangovideos.services.neo4j;

import com.google.api.client.util.Lists;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.services.Interfaces.SongService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Result;
import org.neo4j.graphdb.Transaction;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

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

            final ImmutableMap<String, Object> parameters = ImmutableMap.of(
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
        final ImmutableMap<String, Object> parameters = ImmutableMap.of("videoId", videoId, "index", index);

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
    public List<String> listNames() {
        return listByField("name");
    }

    @Override
    public List<String> listOrquestras() {
        return listByField("orquestra");
    }

    private List<String> listByField(String field) {
        List<String> names;
        final String query = String.format(
                "MATCH (s:Song) " +
                        "WHERE exists(s.%s) " +
                        "RETURN DISTINCT s.%s as result", field, field);
        try (
                final Transaction tx = graphDb.beginTx();
                final Result result = graphDb.execute(query)
        ) {
            names = Lists.newArrayList(result.<String>columnAs("result"));
            tx.success();
        }
        return names;
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
