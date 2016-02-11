import {Component} from 'angular2/core';
import {Http} from 'angular2/http';
import {VideoPreview} from '../videoPreview/videoPreview';
import {Youtube} from "../../services/youtube";
import {VideoService} from "../../services/VideoService";

@Component({
    selector: 'video-form',
    template: require('./videoForm.html'),
    styles: [require('./videoForm.css')],
    providers: [Youtube],
    directives: [VideoPreview],
    pipes: []
})
export class VideoForm {
    value:String = 'https://www.youtube.com/watch?v=6D8uUFj8_4g';
    video:any;
    exists:boolean = true;

    updateValue(url) {
        this.fetch(url)
    }

    constructor(private youtube:Youtube, private videoService:VideoService) {
    }


    add(url:String){
        let id = url.split('?v=')[1];
        this.videoService.add(id);
    }
    fetch(url:String) {
        let id = url.split('?v=')[1];
        this.youtube.getVideo(id)
            .filter(data => data.items.length)
            .subscribe(data => {
                //noinspection TypeScriptUnresolvedVariable
                this.video = data.items[0].snippet;
                this.video.id = id;
            });
        this.videoService.exists(id).subscribe((exists)=> {
            this.exists = exists;
        });
    }
}
