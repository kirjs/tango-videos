import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {VideoInfo} from "../video-info/video-info";

import {PlayerService} from "../../../../services/PlayerService";
import {NeedsPermission} from "../../../common/needs-permission/needs-permission";
import {Video} from "../../../../interfaces/video";

@Component({
    selector: 'video-tile',
    template: require('./video-tile.html'),
    styles: [require('./video-tile.css')],
    providers: [],
    directives: [VideoInfo, NeedsPermission],
    pipes: []
})
export class VideoTile {
    @Input() video: Video;
    @Input() readonly: boolean = true;
    @Output() play:EventEmitter<any>;
    constructor() {
        this.play = new EventEmitter();
    }

    playVideo(){
        this.play.emit(this.video);
    }

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${this.video.id}/mqdefault.jpg`;
    }
}
