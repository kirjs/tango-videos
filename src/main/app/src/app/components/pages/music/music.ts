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
    genres:Observable<Array<any>>;

    fetch() {
        var tenItems = a=>a.slice(0, 10).filter(i=>i.value);

        this.orquestras = this.songService.listOrquestras().map(tenItems);
        this.songs = this.songService.listNames().map(tenItems);
        this.genres = this.songService.listGenres().map(tenItems);
    }

    constructor(private songService:SongService) {
        this.fetch();
    }
}
