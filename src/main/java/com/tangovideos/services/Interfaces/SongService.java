package com.tangovideos.services.Interfaces;

import com.tangovideos.models.Song;

public interface SongService {
    Song insert(String videoId, Integer index, String field, String value);

}
