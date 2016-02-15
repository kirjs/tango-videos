import {Component, Output, EventEmitter, Input} from 'angular2/core';
import {Focus} from "../focus/focus";
import {NgAutocompleteContainer} from "../autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../autocomplete/ng2-autocomplete";

@Component({
    selector: 'addable-field',
    template: require('./addable-field.html'),
    styles: [require('./addable-field.css')],
    providers: [],
    directives: [Focus, NgAutocompleteContainer, NgAutocompleteInput],
    pipes: []
})
export class AddableFIeld {
    @Output() onAdd;
    @Input() autocompleteSource;

    addMode:boolean = false;

    switchToAddMode() {
        this.addMode = true;
    }

    switchToNormalMode() {
        this.addMode = false;
    }

    handleKeyup($event, value:String) {
        if ($event.keyCode == 13) {
            this.add(value)
        }
    }

    add(value:String) {
        this.switchToNormalMode();
        this.onAdd.emit(value);
    }

    constructor() {
        this.onAdd = new EventEmitter();
    }

}
