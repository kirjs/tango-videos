import {Injectable} from 'angular2/core';
var YouTubePlayer = require('youtube-player');

@Injectable()
export class PlayerService {
    player:any;
    playing:boolean = false;

    constructor() {

    }

    isPlaying(){
        return this.playing;
    }

    play(videoId){
        this.playing = true;
        this.player.loadVideoById(videoId);
        this.player.setSize(450, 350);

    }

    init(element:any):void {
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