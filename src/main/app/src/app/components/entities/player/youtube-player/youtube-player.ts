import {Component, ViewChild} from 'angular2/core';
import {PlayerService} from "../../../../services/PlayerService";
import {Video} from "../../../../interfaces/video";

@Component({
    selector: 'youtube-player',
    template: require('./youtube-player.html'),
    styles: [require('./youtube-player.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class YoutubePlayer {
    @ViewChild("player") player;
    private video:Video;
    private autoplay:boolean = true;

    isPlaying() {
        return this.playerService.isPlaying();
    }

    getVideo() {
        return this.video
    }

    stopPlaying() {
        this.playerService.stop();
    }

    setVideo(video:any):void {
        this.video = video;
    }

    next() {
        this.playerService.next()
    }

    setAutoplay(autoplay: boolean) {
        this.autoplay = autoplay;
    }

    getAutoplay() {
        return this.autoplay;
    }

    prev() {
        this.playerService.prev()
    }


    constructor(private playerService:PlayerService) {
    }

    ngAfterViewInit() {
        this.playerService.init(this.player.nativeElement, this);
    }


}
