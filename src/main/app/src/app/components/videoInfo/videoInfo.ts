import {Component, Input} from 'angular2/core';
import {DancerTile} from "../dancer-tile/dancer-tile";
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'video-info',
    template: require('./videoInfo.html'),
    styles: [require('./videoInfo.css')],
    providers: [],
    directives: [DancerTile],
    pipes: []
})
export class VideoInfo {
    @Input() video:any;


    addDancer(name:string) {
        this.videoService.addDancer(this.video.id, name).subscribe((data)=>{
            this.video.dancers = data;
        });
    }

    removeDancer(name:string) {
        this.videoService.removeDancer(this.video.id, name).subscribe((data)=>{
            this.video.dancers = data;
        });
    }


    constructor(private videoService: VideoService) {


    }
}
