import {Injectable, NgZone} from '@angular/core';
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

    constructor(private zone: NgZone) {

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
        this.currentVideoIndex--;
        if (this.currentVideoIndex < 0) {
            this.currentVideoIndex = this.playlist.length - 1;
        }

        this.play(this.playlist, this.playlist[this.currentVideoIndex]);
    }


    playVideo(video:Video) {
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

        this.player.on('stateChange',(event) => {
            this.zone.run(()=> {
                if(event.data == 0){
                    if(this.youtubePlayer.getAutoplay()){
                        this.next();
                    }
                }
            });

        });
    }

    stop():void {
        this.currentVideo = null;
        this.playing = false;
        this.player.stopVideo();
    }
}
