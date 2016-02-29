package com.tangovideos.models;

import java.util.Set;

public class VideoAndDancer {
    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public Set<String> getDancers() {
        return dancers;
    }

    public void setDancers(Set<String> dancers) {
        this.dancers = dancers;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    String videoId;
    Set<String> dancers;
    String title;
    String description;
}
