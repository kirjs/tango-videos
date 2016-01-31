package com.tangovideos.models;

import java.util.ArrayList;

public class Video {
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
