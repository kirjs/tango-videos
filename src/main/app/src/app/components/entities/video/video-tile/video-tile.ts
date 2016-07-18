import {Component, Input, Output, EventEmitter} from '@angular/core';
import {VideoInfo} from "../video-info/video-info";


import {NeedsPermission} from "../../../common/needsPermission/needsPermission";
import {Video} from "../../../../interfaces/video";
import {YoutubeThumb} from "../../../common/youtubeThumb/youtubeThumb";

@Component({
    selector: 'video-tile',
    template: require('./video-tile.html'),
    styles: [require('./video-tile.css')],
    providers: [],
    directives: [VideoInfo, NeedsPermission, YoutubeThumb],
    pipes: []
})
export class VideoTile {
    @Input() video:Video;
    @Input() readonly:boolean = true;
    @Output() play:EventEmitter<any>;

    constructor() {
        this.play = new EventEmitter();
    }

    playVideo() {
        this.play.emit(this.video);
    }

    getGenre() {
        if (!this.video.songs || !this.video.songs[0]) {
            return '';
        }
        return this.video.songs[0].genre && this.video.songs[0].genre.toLowerCase() || '';
    }
}
