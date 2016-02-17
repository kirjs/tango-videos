import {Component, Input} from 'angular2/core';
import {DancerTile} from "../../dancer/dancer-tile/dancer-tile";
import {SongList} from "../../song/song-list/song-list";
import {EditableField} from "../../../common/editable-field/editable-field";
import {Icon} from "../../../common/icon/icon";
import {NeedsPermission} from "../../../common/needs-permission/needs-permission";
import {VideoService} from "../../../../services/VideoService";


@Component({
    selector: 'video-info',
    template: require('./video-info.html'),
    styles: [require('./video-info.css')],
    providers: [],
    directives: [DancerTile, SongList, EditableField, Icon, NeedsPermission],
    pipes: []
})
export class VideoInfo {
    @Input() video:any;
    @Input() readonly:boolean = true;

    // TODO: We have 2 different date formats here, need to standardize
    // TODO: Ugly hack
    toDate(publishedAt:string, recordedAt:number) {
        if (recordedAt) {
            return (recordedAt == +recordedAt ) ? new Date(recordedAt * 1000) : new Date(recordedAt);
        } else {
            return new Date(publishedAt);
        }
    }

    handleUpdate(field, value) {
        if (field == 'recordedAt') {
            value = Date.parse(value) / 1000;
        }

        this.videoService.update(this.video.id, field, value).subscribe(x => {
        });
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

    recover() {
        this.videoService.hide(this.video.id, false).subscribe(() => {
            this.video.shown = false;
        })
    }

    hide() {
        this.videoService.hide(this.video.id, true).subscribe(() => {
            this.video.shown = true;
        })
    }


    markComplete(value:boolean) {
        this.videoService.markComplete(this.video.id, value).subscribe(() => {
            this.video.complete = value;
        }, (error) => {
            //noinspection TypeScriptUnresolvedFunction
            var errorName = error.json().name;
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
