import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {EditableField} from "../editable-field/editable-field";
import {DancerService} from "../../services/DancerService";
import {Observable} from 'rxjs';
import {SongService} from "../../services/SongService";

@Component({
    selector: 'song-item',
    template: require('./song-item.html'),
    styles: [require('./song-item.css')],
    providers: [],
    directives: [EditableField],
    pipes: []
})
export class SongItem {
    @Input() song;
    @Output() update = new EventEmitter();
    yearSource:Observable<Array<string>>;
    songSource:Observable<Array<string>>;
    orquestraSource:Observable<Array<string>>;
    song:Object = {};
    autocompleteSource:any = {};

    handleUpdate(field, data) {
        this.update.emit({field, data});
    }

    constructor(songService: SongService) {
        this.yearSource = Observable.from([["1900", "1934"]]);
        this.orquestraSource = songService.listOrquestras();
        this.songSource =  songService.listNames();
    }


}
