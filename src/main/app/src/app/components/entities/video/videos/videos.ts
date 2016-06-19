import {Component, Input} from '@angular/core';
import {VideoInfo} from "../video-info/video-info";
import {VideoTile} from "../video-tile/video-tile";
import {EditableField} from "../../../common/editable-field/editable-field";
import {PlayerService} from "../../../../services/PlayerService";
import {Video} from "../../../../interfaces/video";

@Component({
    selector: 'videos',
    template: require('./videos.html'),
    styles: [require('./videos.css')],
    providers: [],
    directives: [ VideoInfo, EditableField, VideoTile],
    pipes: []
})
export class Videos {
    @Input() videos:Array<Video> = [];

    playVideo(video){
        this.playerService.play(this.videos, video);
    }

    constructor(private playerService: PlayerService){

    }
}
