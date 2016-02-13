import {Injectable} from 'angular2/core';
var YouTubePlayer = require('youtube-player');

@Injectable()
export class PlayerService {
    player:any;

    constructor() {

    }

    play(videoId){
        this.player.loadVideoById(videoId);
        this.player.playVideo();
    }

    init(nativeElement:any):void {
        this.player = YouTubePlayer(nativeElement);
    }
}