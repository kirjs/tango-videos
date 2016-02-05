import {Component, Input} from 'angular2/core';
import {VideoService} from "../../services/VideoService";
import {VideoInfo} from "../videoInfo/videoInfo";

@Component({
    selector: 'videos',
    template: require('./videos.html'),
    styles: [require('./videos.css')],
    providers: [],
    directives: [ VideoInfo],
    pipes: []
})
export class Videos {
    @Input() videos:Array<any> = [];

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${id}/hqdefault.jpg`;
    }
}
