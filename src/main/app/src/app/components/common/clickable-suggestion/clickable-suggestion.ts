import {Component, Input, Output, EventEmitter} from '@angular/core';
import {KeyValue} from "../../../interfaces/keyValue";

var removeDiacritics = require('diacritics').remove;

@Component({
    selector: 'clickable-suggestion',
    template: require('./clickable-suggestion.html'),
    styles: [require('./clickable-suggestion.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class ClickableSuggestion {
    @Input() label:String = 'Suggested items';
    @Input() text:String = '';
    @Input() options:Array<KeyValue> = [];
    @Input() exclude:Array<String> = [];
    @Output() addSuggestion;
    private suggestions:Array<KeyValue>;

    constructor() {
        this.addSuggestion = new EventEmitter();
    }

    handleAddSuggestion(suggestion) {
        this.addSuggestion.emit(suggestion);
    }

    ngOnChanges() {
        var text = removeDiacritics(this.text)
            .replace('´', '\'').toLowerCase();

        this.suggestions = this.options.filter((option)=> {
            return text.indexOf(removeDiacritics(option.key).toLowerCase().trim()) > -1;
        }).filter((option)=> {
            return this.exclude.indexOf(option.key) === -1;
        }).filter(option=>option.key.length > 2);
    }

}
