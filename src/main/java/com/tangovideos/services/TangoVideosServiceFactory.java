package com.tangovideos.services;

import com.tangovideos.services.Interfaces.*;
import com.tangovideos.services.combined._CombinedVideoService;
import com.tangovideos.services.neo4j.Neo4JServiceFactory;

public class TangoVideosServiceFactory {
    public static ServiceFactory serviceFactory = new Neo4JServiceFactory(true);
    private static Object songService;
    private static DancerService channelService;

    public static UserService getUserService() {
        return serviceFactory.getUserService();
    }

    public static VideoService getVideoService() {
        return serviceFactory.getVideoService();
    }

    public static YoutubeService getYoutubeService() {
        return serviceFactory.getYoutubeService();
    }

    public static DancerService getDancerService() {return serviceFactory.getDancerService();}
    public static AdminToolsService getAdminToolsService(){return serviceFactory.getAdminToolsService();}

    public static SongService getSongService() {
        return serviceFactory.getSongService();
    }

    public static ChannelService getChannelService() {
        return serviceFactory.getChannelService();
    }

    public static void setChannelService(DancerService channelService) {
        TangoVideosServiceFactory.channelService = channelService;
    }

    public static _CombinedVideoService getCombinedVideoService() {
        return serviceFactory.getCombinedVideoService();
    }
}
