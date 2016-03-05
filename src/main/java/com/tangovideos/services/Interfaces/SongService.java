package com.tangovideos.services.Interfaces;

import com.tangovideos.models.ValueWithCount;
import com.tangovideos.models.Song;

import java.util.List;

public interface SongService {
    Song updateField(String videoId, Integer index, String field, String value);

    Song getSong(String videoId, Integer index);


    List<Song> list();

    List<ValueWithCount<String>> listNames();

    List<ValueWithCount<String>> listOrquestras();

    List<ValueWithCount<String>>  listGenres();
}
