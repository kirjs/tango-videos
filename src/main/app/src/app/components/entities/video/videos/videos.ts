import {Component, Input} from 'angular2/core';
import {VideoInfo} from "../video-info/video-info";
import {VideoTile} from "../video-tile/video-tile";
import {EditableField} from "../../../common/editable-field/editable-field";

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
