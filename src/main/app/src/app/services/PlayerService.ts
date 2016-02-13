import {Injectable} from 'angular2/core';
var YouTubePlayer = require('youtube-player');

@Injectable()
export class VideoService {
    player: any;
    constructor(){
        this.player = YouTubePlayer('player');
        this.player.loadVideoById('M7lc1UVf-VE');
        this.player.playVideo();
    }


}