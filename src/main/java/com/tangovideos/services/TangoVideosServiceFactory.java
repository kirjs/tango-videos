package com.tangovideos.services;

import com.tangovideos.models.Video;
import com.tangovideos.services.Interfaces.DancerService;
import com.tangovideos.services.Interfaces.ServiceFactory;
import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.Interfaces.VideoService;
import com.tangovideos.services.neo4j.Neo4JServiceFactory;

public class TangoVideosServiceFactory {
    public static ServiceFactory serviceFactory = new Neo4JServiceFactory();
    public static UserService getUserService(){
        return serviceFactory.getUserService();
    }

    public static VideoService getVideoService(){
        return serviceFactory.getVideoService();
    }

    public static DancerService getDancerService() {
        return serviceFactory.getDancerService();
    }
}
