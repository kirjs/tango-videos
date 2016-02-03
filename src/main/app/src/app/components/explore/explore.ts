import {Component} from 'angular2/core';
import {Videos} from "../videos/videos";
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'explore',
    templateUrl: 'app/components/explore/explore.html',
    styleUrls: ['app/components/explore/explore.css'],
    providers: [],
    directives: [Videos],
    pipes: []
})
export class Explore {
    videos:Array<any> = [];

    constructor(private videoService:VideoService) {
        videoService.list().subscribe((videos)=> {
            this.videos = videos;
        });
    }
}
