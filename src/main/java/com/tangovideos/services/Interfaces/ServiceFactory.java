package com.tangovideos.services.Interfaces;

import com.tangovideos.services.YoutubeService;

public interface ServiceFactory {
    UserService getUserService();
    VideoService getVideoService();
    DancerService getDancerService();
    YoutubeService getYoutubeService();
}
