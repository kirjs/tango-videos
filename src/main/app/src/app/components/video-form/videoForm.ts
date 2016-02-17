import {Component} from 'angular2/core';
import {Http} from 'angular2/http';
import {VideoPreview} from '../video-preview/videoPreview';
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
    value:String = '';
    video:any;
    loading: boolean = false;
    exists:boolean = true;

    updateValue(url) {
        this.fetch(url)
    }

    constructor(private youtube:Youtube, private videoService:VideoService) {
    }


    add( input: HTMLInputElement){
        this.loading = true;
        let id = input.value.split('?v=')[1];
        this.videoService.add(id).subscribe(()=>{
            this.loading = false;
            input.value = '';
        });
    }
    fetch(url:String) {
        console.log(url);
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
