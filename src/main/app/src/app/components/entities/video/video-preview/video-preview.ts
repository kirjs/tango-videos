import {Component, Input} from '@angular/core';
import {Http} from '@angular/http';
import {VideoInfo} from "../video-info/video-info";
import {VideoTile} from "../video-tile/video-tile";


@Component({
    selector: 'video-preview',
    template: require('./video-preview.html'),
    styles: [require('./video-preview.css')],
    providers: [],
    directives: [VideoTile],
})
export class VideoPreview {
    @Input() video:string;
}
