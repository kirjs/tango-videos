import {Component, Input} from 'angular2/core';
import {VideoInfo} from "../videoInfo/videoInfo";
import {PlayerService} from "../../services/PlayerService";

@Component({
    selector: 'video-tile',
    template: require('./video-tile.html'),
    styles: [require('./video-tile.css')],
    providers: [],
    directives: [VideoInfo],
    pipes: []
})
export class VideoTile {
    @Input() video: any;
    @Input() readonly: boolean = true;
    constructor(private playerService: PlayerService) {
    }

    playVideo(id: String){
        this.playerService.play(id);
    }

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${id}/mqdefault.jpg`;
    }
}
