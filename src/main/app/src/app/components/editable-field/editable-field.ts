import {Component, Input, Output, EventEmitter} from 'angular2/core';
import {NgAutocompleteContainer} from "../autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../autocomplete/ng2-autocomplete";


@Component({
    selector: 'editable-field',
    template: require('./editable-field.html'),
    styles: [require('./editable-field.css')],
    providers: [],
    directives: [NgAutocompleteContainer, NgAutocompleteInput],
    pipes: []
})
export class EditableField {
    @Input() value:String = '';
    @Input() emptyValue:String = '';
    @Input() autocompleteSource:Array<String> = [];
    @Output() update = new EventEmitter();


    editMode:boolean = false;

    changeValue(value:string) {
        this.update.emit(value);
        this.value = value;
    }

    constructor() {
    }

}
