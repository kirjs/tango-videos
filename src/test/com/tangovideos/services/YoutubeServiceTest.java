package com.tangovideos.services;

import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
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
        final List<Video> videos = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw", 0);
        assertEquals(videos.size(), 17);

        final List<Video> videosAfterOct18 = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw",
                Instant.parse("2015-10-17T00:00:00.000Z").getEpochSecond());
        assertEquals(videosAfterOct18.size(), 6);

        final List<Video> videosAfterTheSecondToLast = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw", 1455840400);
        assertEquals(videosAfterTheSecondToLast.size(), 1);
    }

    @Test
    public void  testTime(){
        final long time1 = Instant.parse("2015-05-31T17:50:32.000Z").getEpochSecond();
    }

}
