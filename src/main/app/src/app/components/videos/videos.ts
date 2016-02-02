import {Component} from 'angular2/core';
import {VideoService} from "../../services/VideoService";
import {VideoInfo} from "../videoInfo/videoInfo";

@Component({
    selector: 'videos',
    templateUrl: 'app/components/videos/videos.html',
    styleUrls: ['app/components/videos/videos.css'],
    providers: [],
    directives: [ VideoInfo],
    pipes: []
})
export class Videos {
    videos:Array<any> = [];

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${id}/hqdefault.jpg`;
    }

    constructor(private videoService:VideoService) {
        videoService.list().subscribe((videos)=> {
            this.videos = videos;
        });
    }


}
