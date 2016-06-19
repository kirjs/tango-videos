import {Component} from '@angular/core';
import {Videos} from "../../entities/video/videos/videos";
import {LoadMore} from "../../common/load-more/load-more";
import {VideoService} from "../../../services/VideoService";

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
    limit:number = 20;
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
