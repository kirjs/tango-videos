import {Component, Input, Output, EventEmitter} from 'angular2/core';

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
    @Input() options:Array<String> = [];
    @Input() exclude:Array<String> = [];
    @Output() addSuggestion;
    private suggestions:Array<String>;

    constructor() {
        this.addSuggestion = new EventEmitter();
    }

    handleAddSuggestion(suggestion) {
        this.addSuggestion.emit(suggestion);
    }

    ngOnChanges() {
        var text = removeDiacritics(this.text).toLowerCase();
        this.suggestions = this.options.filter((option)=> {
            return text.indexOf(removeDiacritics(option).toLowerCase().trim()) > -1;
        }).filter((option)=> {
            return this.exclude.indexOf(option) === -1;
        }).filter(option=>option.length > 2);
    }

}
