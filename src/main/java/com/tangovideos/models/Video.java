package com.tangovideos.models;

import com.google.api.client.util.Lists;

import java.util.ArrayList;
import java.util.Set;

public class Video {
    private String description;

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

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public void setSongs(ArrayList<Song> songs) {
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
    ArrayList<Song> songs = Lists.newArrayList();
    String date;
    String url;

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
}
