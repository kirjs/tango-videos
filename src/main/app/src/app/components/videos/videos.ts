import {Component, Input} from 'angular2/core';
import {VideoService} from "../../services/VideoService";
import {VideoInfo} from "../videoInfo/videoInfo";
import {EditableField} from "../editable-field/editable-field";
import {VideoTile} from "../video-tile/video-tile";

@Component({
    selector: 'videos',
    template: require('./videos.html'),
    styles: [require('./videos.css')],
    providers: [],
    directives: [ VideoInfo, EditableField, VideoTile],
    pipes: []
})
export class Videos {
    @Input() videos:Array<any> = [];
}
