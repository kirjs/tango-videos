import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';
import {DancerService} from "../../services/DancerService";
import {Observable} from "rxjs";
import {NgAutocompleteContainer} from "../autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../autocomplete/ng2-autocomplete";


@Component({
    selector: 'dancer-tile',
    template: require('./dancer-tile.html'),
    styles: [require('./dancer-tile.css'), require('../common/list.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, NgAutocompleteContainer, NgAutocompleteInput],
    pipes: []
})
export class DancerTile {
    @Input() dancers:any = [];
    @Output() add = new EventEmitter();
    @Output() remove = new EventEmitter();
    private dancersSource:Observable<String>;


    constructor(dancers:DancerService) {
        this.dancersSource = dancers.list().map(result=> {
            return result.map((dancer)=> dancer.name);
        });
    }

    addDancer(dancer:String) {
        // TODO: Proper escaping
        dancer = dancer.replace('/', ' ').trim();
        this.add.emit(dancer);
    }

    removeDancer(event, dancer) {
        event.preventDefault();
        event.stopPropagation();
        this.remove.emit(dancer);

    }

    addNewInput() {
        this.dancers = this.dancers || [];

        if (this.dancers.filter(i => i == '').length == 0) {
            this.dancers.push('')
        }
    }
}
