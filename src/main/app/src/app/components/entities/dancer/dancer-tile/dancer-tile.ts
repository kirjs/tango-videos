import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

import {Observable} from "rxjs";
import {NgAutocompleteContainer} from "../../../common/autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../../../common/autocomplete/ng2-autocomplete";
import {AddableFIeld} from "../../../common/addable-field/addable-field";
import {Icon} from "../../../common/icon/icon";
import {DancerService} from "../../../../services/DancerService";


@Component({
    selector: 'dancer-tile',
    template: require('./dancer-tile.html'),
    styles: [require('./dancer-tile.css'), require('../../../common/list.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, NgAutocompleteContainer, NgAutocompleteInput, AddableFIeld, Icon],
    pipes: []
})
export class DancerTile {
    @Input() dancers:any = [];
    @Input() readonly:boolean = true;
    @Output() add = new EventEmitter();
    @Output() remove = new EventEmitter();
    private dancersSource:Observable<String>;

    constructor(dancers:DancerService) {
        this.dancersSource = dancers.listNames().map(result=> {
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
}
