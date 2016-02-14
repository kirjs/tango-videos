import {Component, Input} from 'angular2/core';
import {DancerTile} from "../dancer-tile/dancer-tile";
import {VideoService} from "../../services/VideoService";
import {SongList} from "../song-list/song-list";
import {EditableField} from "../editable-field/editable-field";

@Component({
    selector: 'video-info',
    template: require('./videoInfo.html'),
    styles: [require('./videoInfo.css')],
    providers: [],
    directives: [DancerTile, SongList, EditableField],
    pipes: []
})
export class VideoInfo {
    @Input() video:any;
    @Input() readonly:boolean = true;

    toDate(date:string){
        return new Date(date);
    }


    addDancer(name:string) {
        this.videoService.addDancer(this.video.id, name).subscribe((data)=> {
            this.video.dancers = data;
        });
    }

    updateSong(info) {
        this.videoService.updateSongInfo(this.video.id, info.index, info.field, info.data).subscribe((song)=> {
            this.video.songs[info.index] = song;
        });
    }

    recover(){
        throw "not implemented";
    }

    hide(){
        this.videoService.hide(this.video.id).subscribe(() => {
            this.video.hidden = true;
        })
    }
    removeDancer(name:string) {
        this.videoService.removeDancer(this.video.id, name).subscribe((data)=> {
            this.video.dancers = data;
        });
    }

    constructor(private videoService:VideoService) {


    }
}
