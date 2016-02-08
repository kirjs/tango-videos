import {Component} from 'angular2/core';
import {Videos} from "../videos/videos";
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'explore',
    template: require('./explore.html'),
    styles: [require('./explore.css')],
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
