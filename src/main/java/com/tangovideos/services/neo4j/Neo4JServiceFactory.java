package com.tangovideos.services.neo4j;

import com.google.common.collect.ImmutableSet;
import com.tangovideos.services.Interfaces.*;
import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined.CombinedVideoService;
import com.tangovideos.services.nameParsing.NameAwareNameParser;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import java.util.HashSet;
import java.util.Set;

public class Neo4JServiceFactory implements ServiceFactory {

    final GraphDatabaseService graphDb = new Neo4DB().graphDb;
    private UserService userService;
    private VideoService videoService;
    private Neo4JRoleService roleService;
    private Neo4jPermissionsService permissionService;
    private YoutubeService youtubeService;
    private DancerService dancerService;
    private SongService songService;
    private AdminToolsService adminToolsService;
    private ChannelService channelService;
    private CombinedVideoService combinedVideoService;


    public Neo4JServiceFactory() {

    }

    public Neo4JServiceFactory(boolean b) {
        if (b) {
            fillDb();
        }
    }


    public void fillDb() {
        try (Transaction tx = this.graphDb.beginTx()) {
            if (!this.getUserService().userExists("admin")) {
                final HashSet<Node> nodes = this.getPermissionService()
                        .addPermissions(ImmutableSet.of("video:read", "video:write"));

                final Node adminRole = this.getRoleService()
                        .addRole("admin", nodes);

                this.getUserService().addUser("admin", "hashme", adminRole);
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
            final Set<Set<String>> dancers = this.getDancerService().getaAllDancersByVideo();
            final NameAwareNameParser nameAwareNameParser = new NameAwareNameParser(dancers);
            youtubeService = new YoutubeService(nameAwareNameParser);
        }

        return youtubeService;
    }

    @Override
    public SongService getSongService() {
        if (songService == null) {
            songService = new Neo4jSongService(graphDb);
        }
        return songService;
    }

    @Override
    public AdminToolsService getAdminToolsService() {
        if (adminToolsService == null) {
            adminToolsService = new Neo4jAdminToolsService(graphDb);
        }

        return adminToolsService;
    }

    @Override
    public ChannelService getChannelService() {
        if (channelService == null) {
            channelService = new Neo4jChannelService(graphDb);
        }

        return channelService;
    }

    @Override
    public CombinedVideoService getCombinedVideoService() {
        if (combinedVideoService == null) {
            combinedVideoService = new CombinedVideoService(
                    getDancerService(),
                    getVideoService(),
                    getChannelService(),
                    getYoutubeService()
            );
        }

        return combinedVideoService;
    }

    @Override
    public DancerService getDancerService() {
        if (dancerService == null) {
            dancerService = new Neo4jDancerService(graphDb);
        }

        return dancerService;
    }
}
