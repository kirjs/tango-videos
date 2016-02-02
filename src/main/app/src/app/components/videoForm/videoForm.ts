import {Component} from 'angular2/core';
import {Http} from 'angular2/http';
import {VideoPreview} from '../videoPreview/videoPreview';

@Component({
    selector: 'video-form',
    templateUrl: 'app/components/videoForm/videoForm.html',
    styleUrls: ['app/components/videoForm/videoForm.css'],
    providers: [],
    directives: [VideoPreview],
    pipes: []
})
export class VideoForm {
    fetchVideoData() {

    }

    constructor(http:Http) {

    }
}
