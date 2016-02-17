import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';
import {SongItem} from "../song-item/song-item";

@Component({
    selector: 'song-list',
    template: require('./song-list.html'),
    styles: [require('./song-list.css'), require('../../../common/list.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, SongItem],
    pipes: []
})
export class SongList {
    @Input() songs:any = [];
    @Input() readonly: boolean = true;
    @Output() add = new EventEmitter();
    @Output() remove = new EventEmitter();
    @Output() update = new EventEmitter();

    constructor() {
    }

    handleUpdate(index, $event){
        $event.index = index;
        this.update.emit($event);
    }
    addItem(item: String) {
        // TODO: Proper escaping
        item = item.replace('/', ' ');
        this.add.emit(item);
    }

    removeSong(event, dancer){
        event.preventDefault();
        event.stopPropagation();
        this.remove.emit(dancer);
    }



    addNewInput() {
        this.songs = this.songs || [];

        if (this.songs.filter(i => i == '').length == 0) {
            this.songs.push({});
        }
    }
}
