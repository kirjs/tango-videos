import {Component, ViewChild} from 'angular2/core';
import {PlayerService} from "../../services/PlayerService";
import {VideoInfo} from "../videoInfo/videoInfo";

@Component({
    selector: 'video-player',
    template: require('./video-player.html'),
    styles: [require('./video-player.css')],
    providers: [],
    directives: [VideoInfo],
    pipes: []
})
export class VideoPlayer {
    @ViewChild("player") player;
    private video:any;

    ngAfterViewInit() {
        this.playerService.init(this.player.nativeElement, this);
    }

    close(){
        this.playerService.stop();
    }

    isPlaying(){
        return this.playerService.isPlaying();
    }

    constructor(private playerService:PlayerService) {
    }

    setVideo(video:any):void {
        this.video = video;

    }
}
