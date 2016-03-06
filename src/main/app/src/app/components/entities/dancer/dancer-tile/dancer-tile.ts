import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {Router} from 'angular2/router';

import {Observable} from "rxjs";
import {NgAutocompleteContainer} from "../../../common/autocomplete/ng2-autocomplete";
import {NgAutocompleteInput} from "../../../common/autocomplete/ng2-autocomplete";
import {AddableField} from "../../../common/addable-field/addable-field";
import {Icon} from "../../../common/icon/icon";
import {DancerService} from "../../../../services/DancerService";
import {EditableList} from "../../../common/editable-list/editable-list";


@Component({
    selector: 'dancer-tile',
    template: require('./dancer-tile.html'),
    styles: [require('./dancer-tile.css'), require('../../../common/list.css')],
    providers: [],
    directives: [NgAutocompleteContainer, NgAutocompleteInput, AddableField, Icon, EditableList],
    pipes: []
})
export class DancerTile {
    @Input() dancers:any = [];
    @Input() readonly:boolean = true;
    @Output() add = new EventEmitter();
    @Output() remove = new EventEmitter();
    private dancersSource:Observable<String>;

    constructor(dancers:DancerService, private router:Router) {
        this.dancersSource = dancers.listNames().map(result=> {
            return result.map((dancer)=> dancer.name);
        });
    }

    selectDancer(dancer) {
        this.router.navigate(['Dancer', {id: dancer}])
    }

    addDancer(dancer:String) {
        this.add.emit(dancer.replace('/', ' '));
    }

    removeDancer(dancer) {
        this.remove.emit(dancer);
    }
}
