import {Component, Input} from 'angular2/core';
import {Dancers} from "../dancers/dancers";
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'video-info',
    templateUrl: 'app/components/videoInfo/videoInfo.html',
    styleUrls: ['app/components/videoInfo/videoInfo.css'],
    providers: [],
    directives: [Dancers],
    pipes: []
})
export class VideoInfo {
    @Input() video:any;


    addDancer(name:string) {
        this.videoService.addDancer(this.video.id, name).subscribe((data)=>{
            this.video.dancers = data;
        });
    }
    constructor(private videoService: VideoService) {


    }
}
