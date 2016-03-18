package com.tangovideos.models;

public class Channel {


    private String id;
    private String uploadPlaylistId;
    private String title;
    private long lastUpdated;
    private long videosCount;
    private boolean autoupdate;

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

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public long getVideosCount() {
        return videosCount;
    }

    public void setVideosCount(long videosCount) {
        this.videosCount = videosCount;
    }

    public boolean getAutoupdate() {
        return autoupdate;
    }

    public boolean isAutoupdate() {
        return autoupdate;
    }

    public void setAutoupdate(boolean autoupdate) {
        this.autoupdate = autoupdate;
    }
}
