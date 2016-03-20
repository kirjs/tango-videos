package com.tangovideos.models;

import com.google.api.client.util.Lists;

import java.util.List;
import java.util.Set;

public class Video {
    private String description;
    private String recordedAt;
    private String addedAt;
    private String type;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public Object getPublishedAt() {
        return publishedAt;
    }

    public Set<String> getDancers() {
        return dancers;
    }

    public void setDancers(Set<String> dancers) {
        this.dancers = dancers;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private final String id;
    String title;
    private final Object publishedAt;
    Set<String> dancers;
    List<Song> songs = Lists.newArrayList();
    String date;
    String url;

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    String channelId;

    public Video(String id, String title, String publishedAt) {
        this.id = id;
        this.title = title;
        this.publishedAt = publishedAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRecordedAt(String recordedAt) {
        this.recordedAt = recordedAt;
    }

    public String getRecordedAt() {
        return recordedAt;
    }

    public void setAddedAt(String addedAt) {
        this.addedAt = addedAt;
    }

    public String getAddedAt() {
        return addedAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
