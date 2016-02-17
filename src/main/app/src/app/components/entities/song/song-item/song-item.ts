import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {Observable} from 'rxjs';
import {EditableField} from "../../../common/editable-field/editable-field";
import {Icon} from "../../../common/icon/icon";
import {SongService} from "../../../../services/SongService";

@Component({
    selector: 'song-item',
    template: require('./song-item.html'),
    styles: [require('./song-item.css')],
    providers: [],
    directives: [EditableField, Icon],
    pipes: []
})
export class SongItem {
    @Input() song = {};
    @Input() readonly: boolean = false;
    @Output() update = new EventEmitter();
    yearSource:Observable<Array<string>>;
    genreSource:Observable<Array<string>>;
    songSource:Observable<Array<string>>;
    orquestraSource:Observable<Array<string>>;
    autocompleteSource:any = {};

    handleUpdate(field, data) {
        this.update.emit({field, data});
    }

    constructor(songService: SongService) {
        this.yearSource = Observable.from([["1900", "1934"]]);
        this.orquestraSource = songService.listOrquestras();
        this.songSource =  songService.listNames();
        this.genreSource =  Observable.from([["Milonga", "Tango", "Vals", "Nuevo", "Non tango"]]);
    }
}
