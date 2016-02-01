import {Component} from 'angular2/core';
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'videos',
    templateUrl: 'app/components/videos/videos.html',
    styleUrls: ['app/components/videos/videos.css'],
    providers: [],
    directives: [],
    pipes: []
})
export class Videos {
    videos:Array = [];
    getThumbnail(id: String){
        return `https://i.ytimg.com/vi/${id}/hqdefault.jpg`;
    }

    constructor(private videoService:VideoService) {
        videoService.list().subscribe((videos)=> {
            this.videos = videos;
        });

    }


}
