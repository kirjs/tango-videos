import {Component, ViewChild} from 'angular2/core';
import {PlayerService} from "../../services/PlayerService";

@Component({
    selector: 'video-player',
    template: require('./video-player.html'),
    styles: [require('./video-player.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class VideoPlayer {

    @ViewChild("player") player;

    ngAfterViewInit() {
        this.playerService.init(this.player.nativeElement);
    }

    close(){
        this.playerService.stop();
    }
    isPlaying(){
        return this.playerService.isPlaying();
    }

    constructor(private playerService:PlayerService) {
    }

}
