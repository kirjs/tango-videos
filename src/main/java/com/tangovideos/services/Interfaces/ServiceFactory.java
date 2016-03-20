package com.tangovideos.services.Interfaces;

import com.tangovideos.services.YoutubeService;
import com.tangovideos.services.combined.CombinedVideoService;

public interface ServiceFactory {
    UserService getUserService();
    VideoService getVideoService();
    DancerService getDancerService();
    YoutubeService getYoutubeService();
    SongService getSongService();
    AdminToolsService getAdminToolsService();
    ChannelService getChannelService();
    CombinedVideoService getCombinedVideoService();
}
