package com.tangovideos.services;

import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class YoutubeServiceTest {

    YoutubeService youtubeService = new YoutubeService();

    @Test
    public void testGetVideoInfo() throws Exception {

    }

    @Ignore @Test
    public void testGetChannelInfo() throws Exception {
        final Channel channel = youtubeService.getChannelInfoById("UC96VDubYr0r1CY1ij7TfxNA");
        assertEquals("Jesús Venezolasi JesusProblemas", channel.getTitle());
        assertEquals("UU96VDubYr0r1CY1ij7TfxNA", channel.getUploadPlaylistId());
    }

    @Ignore @Test
    public void testGetPlaylistVideos() throws Exception {
        final List<Video> videos = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw");
    }


}