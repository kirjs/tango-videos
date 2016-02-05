import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

@Component({
    selector: 'song-list',
    templateUrl: 'app/components/song-list/song-list.html',
    styleUrls: ['app/components/song-list/song-list.css'],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
    pipes: []
})
export class SongList {
    @Input() dancers:any = [];
    @Output() add = new EventEmitter();
    @Output() remove = new EventEmitter();

    constructor() {
    }

    addSong(dancer: String) {
        // TODO: Proper escaping
        dancer = dancer.replace('/', ' ');
        this.add.emit(dancer);
    }

    removeSong(event, dancer){
        event.preventDefault();
        event.stopPropagation();
        this.remove.emit(dancer);

    }

    addNewInput() {
        this.dancers = this.dancers || [];

        if (this.dancers.filter(i => i == '').length == 0) {
            this.dancers.push('')
        }
    }
}
