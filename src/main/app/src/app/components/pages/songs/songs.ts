import {Component} from 'angular2/core';

import {ROUTER_DIRECTIVES} from 'angular2/router';
import {SongService} from "../../../services/SongService";

@Component({
    selector: 'songs',
    template: require('./songs.html'),
    styles: [require('./songs.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
    pipes: []
})
export class Songs {
    songs:Array<any> = [];

    fetch() {
        this.songService.list().subscribe((songs)=> {
            this.songs = songs;
        });
    }

    constructor(private songService:SongService) {
        this.fetch();
    }
}
