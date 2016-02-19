import {Injectable} from 'angular2/core';
import {VideoPlayer} from "../components/entities/player/video-player/video-player";
import {YoutubePlayer} from "../components/entities/player/youtube-player/youtube-player";
import {Video} from "../interfaces/video";
var YouTubePlayer = require('youtube-player');

@Injectable()
export class PlayerService {
    player:any;
    playing:boolean = false;
    private youtubePlayer:YoutubePlayer;
    private currentVideo:Video;
    private playlist:Array<Video>;
    private currentVideoIndex = 0;

    constructor() {

    }

    isPlaying() {
        return this.playing;
    }

    next() {
        this.currentVideoIndex++;
        if (this.currentVideoIndex === this.playlist.length) {
            this.currentVideoIndex = 0;
        }

        this.play(this.playlist, this.playlist[this.currentVideoIndex]);
    }


    prev() {
        var index = this.playlist.indexOf(this.currentVideo) + 1;
        if (index === this.playlist.length) {
            index = 0;
        }
        this.play(this.playlist, this.playlist[index]);
    }


    playVideo(video: Video){
        this.playing = true;
        this.player.loadVideoById(video.id);
        this.player.setSize(450, 350);
        this.youtubePlayer.setVideo(video);
    }

    play(playlist:Array<Video>, video:Video) {
        this.playlist = playlist;
        this.currentVideoIndex = this.playlist.indexOf(video);
        this.playVideo(video);

    }

    init(element:any, youtubePlayer:YoutubePlayer):void {
        this.youtubePlayer = youtubePlayer;

        this.player = YouTubePlayer(element, {
            width: 450,
            height: 350
        });
    }

    stop():void {
        this.currentVideo = null;
        this.playing = false;
        this.player.stopVideo();
    }
}
