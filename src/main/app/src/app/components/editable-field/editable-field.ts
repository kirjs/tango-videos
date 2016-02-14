import {Component, Input, Output, EventEmitter, ViewChild} from 'angular2/core';
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
    @ViewChild("input") input;

    editMode:boolean = false;

    handleKeyup(event){
        if(event.keyCode == 13){
            this.changeValue(event.target.value);
        }
    }

    changeValue(value:string) {
        this.update.emit(value);
        this.value = value;
    }

    constructor() {
    }

}
