import {Component, Input} from 'angular2/core';
import {Http} from 'angular2/http';
import {Youtube} from '../../services/youtube';
import {VideoInfo} from "../videoInfo/videoInfo";
import {VideoTile} from "../video-tile/video-tile";


@Component({
    selector: 'video-preview',
    template: require('./videoPreview.html'),
    styles: [require('./videoPreview.css')],
    providers: [Youtube],
    directives: [VideoTile],
})
export class VideoPreview {
    @Input() src:string;
    videoInfo:any;


    constructor(private youtube:Youtube) {
    }

    ngOnInit() {
        let id = this.src.split('?v=')[1];
        this.youtube.getVideo(id).subscribe(data => {
            //noinspection TypeScriptUnresolvedVariable
            this.videoInfo = data.items[0].snippet;
            this.videoInfo.id = id;


        });
    }

}
