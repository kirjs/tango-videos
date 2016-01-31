package com.tangovideos.services;

import com.tangovideos.services.Interfaces.ServiceFactory;
import com.tangovideos.services.Interfaces.UserService;
import com.tangovideos.services.neo4j.Neo4JServiceFactory;

public class TangoVideosServiceFactory {
    public static ServiceFactory serviceFactory = new Neo4JServiceFactory();
    public static UserService getUserService(){
        return serviceFactory.getUserService();
    }
}
