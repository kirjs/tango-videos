import {Component, Input} from 'angular2/core';
import {Http} from 'angular2/http';
import {Youtube} from '../../services/youtube';
import {VideoInfo} from "../video-info/videoInfo";
import {VideoTile} from "../video-tile/video-tile";


@Component({
    selector: 'video-preview',
    template: require('./videoPreview.html'),
    styles: [require('./videoPreview.css')],
    providers: [],
    directives: [VideoTile],
})
export class VideoPreview {
    @Input() video:string;
}
