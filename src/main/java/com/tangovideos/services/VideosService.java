package com.tangovideos.services;

public class VideosService {
    private static VideosService instance;

    public static VideosService getInstance() {
        return new VideosService();
    }


}
