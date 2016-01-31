package com.tangovideos.services.neo4j;

import com.tangovideos.services.Interfaces.ServiceFactory;
import com.tangovideos.services.Interfaces.UserService;

public class Neo4JServiceFactory implements ServiceFactory {

     final Neo4DB graphDb = new Neo4DB();
     private UserService userService;



     public UserService getUserService(){
          if(userService == null){
               userService = new Neo4jUserService(graphDb.graphDb);
          }

          return userService;
     }
}
