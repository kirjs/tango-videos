package com.tangovideos.services.neo4j;

import com.tangovideos.services.Interfaces.ServiceFactory;
import com.tangovideos.services.Interfaces.UserService;

public class TangoVideosServiceFactory {
    public static ServiceFactory serviceFactory = new Neo4JServiceFactory();
    public static UserService getUserService(){
        return serviceFactory.getUserService();
    }
}
