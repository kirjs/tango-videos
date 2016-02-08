import {Component} from 'angular2/core';
import {Videos} from "../videos/videos";
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'needs-review',
    template: require('./needs-review.html'),
    styles: [require('./needs-review.css')],
    providers: [],
    directives: [Videos],
    pipes: []
})
export class NeedsReview {
    videos:Array<any> = [];

    constructor(private videoService:VideoService) {
        videoService.list().subscribe((videos)=> {
            this.videos = videos;
        });
    }
}
