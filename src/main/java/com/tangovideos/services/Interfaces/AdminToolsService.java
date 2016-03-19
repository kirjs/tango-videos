package com.tangovideos.services.Interfaces;

import com.tangovideos.models.KeyValue;
import com.tangovideos.models.VideoAndDancer;

import java.util.List;

public interface AdminToolsService {
    long renameDancer(String oldName, String newName);

    List<KeyValue> stats();

    List<VideoAndDancer> getVideosAndDancers();
}
