import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {EditableField} from "../editable-field/editable-field";

@Component({
    selector: 'song-item',
    template: require('./song-item.html'),
    styles: [require('./song-item.css')],
    providers: [],
    directives: [EditableField],
    pipes: []
})
export class SongItem {
    @Input() item;
    @Output() update = new EventEmitter();
    song:Object = {};
    handleUpdate(field, data){
        this.update.emit({field, data});
    }


    constructor() {
    }

}
