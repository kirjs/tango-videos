import {Component, Input, Output, EventEmitter} from 'angular2/core';

@Component({
    selector: 'editable-field',
    template: require('./editable-field.html'),
    styles: [require('./editable-field.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class EditableField {
    @Input() value:String = '';
    @Input() emptyValue:String = '';
    @Output() update = new EventEmitter();

    editMode:boolean = false;

    changeValue(value:string) {
        this.update.emit(value);
        this.value = value;
    }

    constructor() {
    }

}
