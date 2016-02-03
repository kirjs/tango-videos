package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.services.Interfaces.*;
import com.tangovideos.services.YoutubeService;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashSet;

public class Neo4JServiceFactory implements ServiceFactory {

    final GraphDatabaseService graphDb = new Neo4DB().graphDb;
    private UserService userService;
    private VideoService videoService;
    private Neo4JRoleService roleService;
    private Neo4jPermissionsService permissionService;
    private YoutubeService youtubeService;
    private DancerService dancerService;


    public Neo4JServiceFactory() {
        fillDb();
    }


    private void fillDb() {
        try (Transaction tx = this.graphDb.beginTx()) {
            if (!this.getUserService().userExists("admin")) {
                final HashSet<Node> nodes = this.getPermissionService()
                        .addPermissions(ImmutableSet.of("video:read", "video:write"));

                final Node adminRole = this.getRoleService()
                        .addRole("admin", nodes);

                final Node admin = this.getUserService().addUser("admin", "hashme", adminRole);


                this.getVideoService().addVideo(youtubeService.getVideoInfo("6D8uUFj8_4g"));
                this.getVideoService().addVideo(youtubeService.getVideoInfo("jMUK-IHyBIU"));
            }
            tx.success();
        }
    }

    public UserService getUserService() {
        if (userService == null) {
            userService = new Neo4jUserService(graphDb);
        }
        return userService;
    }

    public RoleService getRoleService() {
        if (roleService == null) {
            roleService = new Neo4JRoleService(graphDb);
        }

        return roleService;
    }


    public VideoService getVideoService() {
        if (videoService == null) {
            videoService = new Neo4jVideoService(graphDb);
        }
        return videoService;
    }

    public PermissionsService getPermissionService() {
        if (permissionService == null) {
            permissionService = new Neo4jPermissionsService(graphDb);
        }
        return permissionService;
    }

    public YoutubeService getYoutubeService() {
        if (youtubeService == null) {
            youtubeService = new YoutubeService();
        }

        return youtubeService;
    }

    public DancerService getDancerService() {
        if (dancerService == null) {
            dancerService = new Neo4jDancerService(graphDb);
        }

        return dancerService;
    }

}
