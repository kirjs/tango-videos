import {Component, ViewChild} from 'angular2/core';
import {VideoInfo} from "../../video/video-info/video-info";
import {PlayerService} from "../../../../services/PlayerService";
import {YoutubePlayer} from "../youtube-player/youtube-player";


@Component({
    selector: 'video-player',
    template: require('./video-player.html'),
    styles: [require('./video-player.css')],
    providers: [],
    directives: [VideoInfo, YoutubePlayer],
    pipes: []
})
export class VideoPlayer {

}
