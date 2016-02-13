package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Song;
import com.tangovideos.services.Interfaces.SongService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import java.util.NoSuchElementException;
import java.util.Set;

public class Neo4jSongService implements SongService {
    final private GraphDatabaseService graphDb;
    final private Set<String> allowedParameters = ImmutableSet.of("year", "name", "orchestra");


    public Neo4jSongService(GraphDatabaseService graphDb) {
        this.graphDb = graphDb;
    }

    @Override
    public Song updateField(String videoId, Integer index, String field, String value) {
        if(!allowedParameters.contains(field)){
            throw new NoSuchElementException(String.format("Invalid parameter: %s", field));
        }
        try (Transaction tx = graphDb.beginTx()) {
            String query = String.format("MATCH (v:Video {id: {videoId}}) " +
                    "MERGE v<-[:PLAYS_IN{index: {index}}]-(s:Song {%s:{value}}) " +
                    "RETURN s as song", field);

            final ImmutableMap<String, Object> parameters = ImmutableMap.of(
                    "videoId", videoId,
                    "index", index,
                    "value", value);

            graphDb.execute(query, parameters);
            tx.success();
        }
        return new Song();
    }
}
