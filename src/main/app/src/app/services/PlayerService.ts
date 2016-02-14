import {Injectable} from 'angular2/core';
import {VideoPlayer} from "../components/video-player/video-player";
var YouTubePlayer = require('youtube-player');

@Injectable()
export class PlayerService {
    player:any;
    playing:boolean = false;
    private videoPlayer:VideoPlayer;

    constructor() {

    }

    isPlaying(){
        return this.playing;
    }

    play(video){
        this.playing = true;
        this.player.loadVideoById(video.id);
        this.player.setSize(450, 350);
        this.videoPlayer.setVideo(video);

    }

    init(element:any, videoPlayer:VideoPlayer):void {
        this.videoPlayer = videoPlayer;

        this.player = YouTubePlayer(element, {
            width: 450,
            height: 350
        });
    }

    stop():void {
        this.playing = false;
        this.player.stopVideo();
    }
}