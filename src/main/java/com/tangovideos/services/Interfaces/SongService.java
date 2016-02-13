package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Song;

import java.util.List;

public interface SongService {
    Song updateField(String videoId, Integer index, String field, String value);

    Song getSong(String videoId, Integer index);


    List<String> listNames();

    List<String> listOrquestras();
}
