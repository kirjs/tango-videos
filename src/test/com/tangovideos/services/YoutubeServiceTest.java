package com.tangovideos.services;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.tangovideos.models.Channel;
import com.tangovideos.models.Video;
import com.tangovideos.services.nameParsing.NameAwareNameParser;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class YoutubeServiceTest {

    YoutubeService youtubeService;

    @Ignore
    @Test
    public void testGetVideoInfo() throws Exception {
        final String videoId = "n6kSrezQTdQ";
        final Video video = youtubeService.getVideoInfo(videoId);
        assertEquals(videoId, video.getId());
        assertEquals("UCgWXMx-Pu9QcW0zepRecnqw", video.getChannelId());
    }

    @Before
    public void before() {
        final NameAwareNameParser nameExtractor = new NameAwareNameParser(
                ImmutableSet.of(ImmutableSet.of("Pablo Rodriguez")),
                ImmutableMap.of());
        youtubeService = new YoutubeService(nameExtractor);
    }

    @Ignore
    @Test
    public void testGetChannelInfo() throws Exception {
        final Channel channel = youtubeService.getChannelInfoById("UC96VDubYr0r1CY1ij7TfxNA");
        assertEquals("Jesús Venezolasi JesusProblemas", channel.getTitle());
        assertEquals("UU96VDubYr0r1CY1ij7TfxNA", channel.getUploadPlaylistId());
    }

    @Ignore
    @Test
    public void testGetPlaylistVideos() throws Exception {
        final List<Video> videos = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw", 0);
        assertEquals(videos.size(), 17);

        final List<Video> videosAfterOct18 = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw",
                Instant.parse("2015-10-17T00:00:00.000Z").getEpochSecond());
        assertEquals(videosAfterOct18.size(), 6);

        assertEquals(ImmutableSet.of("Liz Vanhove", "Yannick Vanhove"), videosAfterOct18.get(0).getDancers());

        final List<Video> videosAfterTheSecondToLast = youtubeService.fetchChannelVideos("UUgWXMx-Pu9QcW0zepRecnqw", 1455840400);
        assertEquals(videosAfterTheSecondToLast.size(), 1);
    }

    @Test
    public void testTime() {
        final long time1 = Instant.parse("2015-05-31T17:50:32.000Z").getEpochSecond();
    }

    @Test
    public void testSnippetToVideo() throws Exception {
        final String videoId = "videoId";
        JSONObject snippet = new JSONObject();
        final String title = "Liz & Yannick Vanhove - Milonga";
        snippet.put("title", title);
        snippet.put("publishedAt", "Date");
        snippet.put("description", "Description");
        snippet.put("channelId", "channelId");
        final Video video = youtubeService.snippetToVideo(videoId, snippet);
        assertEquals(title, video.getTitle());
        assertEquals(ImmutableSet.of("Liz Vanhove", "Yannick Vanhove"), video.getDancers());

    }
}
