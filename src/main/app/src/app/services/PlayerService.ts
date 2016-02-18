import {Injectable} from 'angular2/core';
import {VideoPlayer} from "../components/entities/player/video-player/video-player";
import {YoutubePlayer} from "../components/entities/player/youtube-player/youtube-player";
var YouTubePlayer = require('youtube-player');

@Injectable()
export class PlayerService {
    player:any;
    playing:boolean = false;
    private youtubePlayer:YoutubePlayer;

    constructor() {

    }

    isPlaying(){
        return this.playing;
    }

    play(video){
        this.playing = true;
        this.player.loadVideoById(video.id);
        this.player.setSize(450, 350);
        this.youtubePlayer.setVideo(video);

    }

    init(element:any, youtubePlayer:YoutubePlayer):void {
        this.youtubePlayer = youtubePlayer;

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
