package com.tangovideos.models;

public class Channel {


    private String id;
    private String uploadPlaylistId;
    private String title;

    public Channel(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUploadPlaylistId(String uploadPlaylistId) {
        this.uploadPlaylistId = uploadPlaylistId;
    }

    public String getUploadPlaylistId() {
        return uploadPlaylistId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}