package com.tangovideos.services;

import com.tangovideos.services.Interfaces.*;
import com.tangovideos.services.neo4j.Neo4JServiceFactory;

public class TangoVideosServiceFactory {
    public static ServiceFactory serviceFactory = new Neo4JServiceFactory(true);
    private static Object songService;

    public static UserService getUserService() {
        return serviceFactory.getUserService();
    }

    public static VideoService getVideoService() {
        return serviceFactory.getVideoService();
    }

    public static YoutubeService getYoutubeService() {
        return serviceFactory.getYoutubeService();
    }

    public static DancerService getDancerService() {
        return serviceFactory.getDancerService();
    }

    public static SongService getSongService() {
        return serviceFactory.getSongService();
    }
}
