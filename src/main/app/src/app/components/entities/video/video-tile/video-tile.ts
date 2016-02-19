import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {VideoInfo} from "../video-info/video-info";

import {PlayerService} from "../../../../services/PlayerService";
import {NeedsPermission} from "../../../common/needs-permission/needs-permission";

@Component({
    selector: 'video-tile',
    template: require('./video-tile.html'),
    styles: [require('./video-tile.css')],
    providers: [],
    directives: [VideoInfo, NeedsPermission],
    pipes: []
})
export class VideoTile {
    @Input() video: any;
    @Input() readonly: boolean = true;
    @Output() play:EventEmitter<any>;
    constructor() {
        this.play = new EventEmitter();
    }

    playVideo(id: String){
        this.play.emit(this.video);
    }

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${id}/mqdefault.jpg`;
    }
}
