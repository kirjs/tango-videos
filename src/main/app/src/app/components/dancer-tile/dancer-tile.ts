import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

@Component({
    selector: 'dancer-tile',
    template: require('./dancer-tile.html'),
    styles: [require('./dancer-tile.css'), require('../common/list.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
    pipes: []
})
export class DancerTile {
    @Input() dancers:any = [];
    @Output() add = new EventEmitter();
    @Output() remove = new EventEmitter();

    constructor() {
    }

    addDancer(dancer: String) {
        // TODO: Proper escaping
        dancer = dancer.replace('/', ' ');
        this.add.emit(dancer);
    }

    removeDancer(event, dancer){
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
