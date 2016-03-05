import {Component} from 'angular2/core';

import {ROUTER_DIRECTIVES} from 'angular2/router';
import {SongService} from "../../../services/SongService";
import {Observable} from "rxjs";
@Component({
    selector: 'music',
    template: require('./music.html'),
    styles: [require('./music.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
    pipes: []
})
export class Music {

    orquestras:Observable<Array<any>>;
    songs:Observable<Array<any>>;

    fetch() {
        this.orquestras = this.songService.listOrquestras().map(a=>a.slice(0, 10));
        this.songs = this.songService.listNames().map(a=>a.slice(0, 10));
    }

    constructor(private songService:SongService) {
        this.fetch();
    }
}
