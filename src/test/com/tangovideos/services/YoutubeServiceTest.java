package com.tangovideos.services;

import com.tangovideos.models.Channel;
import org.junit.Ignore;
import org.junit.Test;

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


}