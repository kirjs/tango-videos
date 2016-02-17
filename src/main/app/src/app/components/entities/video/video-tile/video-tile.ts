import {Component, Input} from 'angular2/core';
import {VideoInfo} from "../video-info/video-info";

import {PlayerService} from "../../../../services/PlayerService";
import {NeedsPermission} from "../../../common/needs-permission/needs-permission";

@Component({
    selector: 'video-tile',
    template: require('./video-tile.html'),
    styles: [require('./video-tile.css')],
    providers: [],
    directives: [VideoInfo, NeedsPermission],
    pipes: []
})
export class VideoTile {
    @Input() video: any;
    @Input() readonly: boolean = true;
    constructor(private playerService: PlayerService) {
    }

    playVideo(id: String){
        this.playerService.play(this.video);
    }

    getThumbnail(id:string) {
        return `https://i.ytimg.com/vi/${id}/mqdefault.jpg`;
    }
}
