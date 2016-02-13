import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {EditableField} from "../editable-field/editable-field";
import {DancerService} from "../../services/DancerService";
import {Observable} from 'rxjs';

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
    yearSource:Observable<Array<string>>;
    songSource:Observable<Array<string>>;
    orchestraSource:Observable<Array<string>>;
    song:Object = {};
    autocompleteSource:any = {};

    handleUpdate(field, data) {
        this.update.emit({field, data});
    }

    constructor() {
        this.yearSource = Observable.from([["1900", "1934"]]);
        this.orchestraSource = Observable.from([["Francisco Canaro"]]);
        this.songSource = Observable.fromArray([["Poema"]]);
    }


}
