package com.tangovideos.models;

import java.util.ArrayList;

public class Video {
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

    public ArrayList<Dancer> getDancers() {
        return dancers;
    }

    public void setDancers(ArrayList<Dancer> dancers) {
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
    ArrayList<Dancer> dancers;
    ArrayList<Song> songs;
    String date;
    String url;

    public Video(String id, String title, String publishedAt) {
        this.id = id;
        this.title = title;
        this.publishedAt = publishedAt;
    }
}
