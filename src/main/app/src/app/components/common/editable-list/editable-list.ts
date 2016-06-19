import {Component, Output, Input, EventEmitter} from '@angular/core';
import {Icon} from "../icon/icon";
import {Observable} from "rxjs";
import {AddableField} from "../addable-field/addable-field";
import {NgAutocompleteInput} from "../autocomplete/ng2-autocomplete";
import {NgAutocompleteContainer} from "../autocomplete/ng2-autocomplete";


@Component({
    selector: 'editable-list',
    template: require('./editable-list.html'),
    styles: [require('./editable-list.css'), require('../list.css')],
    providers: [],
    directives: [NgAutocompleteContainer, NgAutocompleteInput, AddableField, Icon],
    pipes: []
})
export class EditableList {
    @Output() addItem:EventEmitter<any>;
    @Output() removeItem:EventEmitter<any>;
    @Output() selectItem:EventEmitter<any>;
    @Input() items:Array<any> = [];
    @Input() readonly:boolean = false;
    @Input() autocompleteSource = Observable.from([[]]);


    handleSelectItem(item) {
        this.selectItem.emit(item);
    }

    handleRemoveItem(event, item) {
        event.preventDefault();
        event.stopPropagation();
        this.removeItem.emit(item);
    }

    handleAddItem(item) {
        this.addItem.emit(item.trim());
    }


    constructor() {
        this.addItem = new EventEmitter();
        this.removeItem = new EventEmitter();
        this.selectItem = new EventEmitter();
    }

}
