import {Component} from 'angular2/core';
import {Videos} from "../videos/videos";
import {VideoService} from "../../services/VideoService";
import {LoadMore} from "../load-more/load-more";

@Component({
    selector: 'explore',
    template: require('./explore.html'),
    styles: [require('./explore.css')],
    providers: [],
    directives: [Videos, LoadMore],
    pipes: []
})
export class Explore {
    videos:Array<any> = [];
    limit:number = 2;
    skip: number = 0;

    fetch(){
        this.videoService.list(this.skip, this.limit).subscribe((videos)=> {
            this.videos = this.videos.concat(videos);
            this.skip = this.videos.length;
        });
    }
    constructor(private videoService:VideoService) {
        this.fetch();
    }
}
