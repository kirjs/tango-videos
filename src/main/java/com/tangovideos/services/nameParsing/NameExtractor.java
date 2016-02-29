package com.tangovideos.services.nameParsing;

import com.tangovideos.models.VideoAndDancer;

import java.util.Set;

public interface NameExtractor {
    Set<String> extractNames(String title, String description);
    Set<String> extractNames(VideoAndDancer videoAndDancers);
    String getLabel();
}
