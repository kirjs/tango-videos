import {Component, Input, Output, EventEmitter, ViewChild} from '@angular/core';
import {Focus} from "../focus/focus";
import { Observable } from 'rxjs';
import {NgAutocompleteContainer} from "../autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../autocomplete/ng2-autocomplete";


@Component({
    selector: 'editable-field',
    template: require('./editable-field.html'),
    styles: [require('./editable-field.css')],
    providers: [],
    directives: [NgAutocompleteContainer, NgAutocompleteInput, Focus],
    pipes: []
})
export class EditableField {
    @Input() value:String = '';
    @Input() emptyValue:String = '';
    @Input() autocompleteSource:Observable<Array<String>> = Observable.from([[]]);
    @Input() readonly: boolean = false;
    @Output() update = new EventEmitter();
    @ViewChild("input") input;

    editMode:boolean = false;

    handleKeyup(event) {
        if (event.keyCode == 13) {
            this.changeValue(event.target.value);
        }
    }

    changeValue(value:string) {
        value = value.trim();
        this.update.emit(value);
        this.value = value;
    }

    constructor() {
    }

}
