import {Component, Input} from 'angular2/core';
import {VideoInfo} from "../videoInfo/videoInfo";

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
    constructor() {
    }

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${id}/mqdefault.jpg`;
    }

}
