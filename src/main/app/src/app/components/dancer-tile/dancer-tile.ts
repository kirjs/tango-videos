import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

@Component({
    selector: 'dancer-tile',
    templateUrl: 'app/components/dancer-tile/dancer-tile.html',
    styleUrls: ['app/components/dancer-tile/dancer-tile.css'],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
    pipes: []
})
export class DancerTile {
    @Input() dancers:any = [];
    @Output() addCallback = new EventEmitter();

    constructor() {
    }

    save(dancer: String) {
        this.addCallback.emit(dancer);
    }

    add() {
        this.dancers = this.dancers || [];

        if (this.dancers.filter(i => i == '').length == 0) {
            this.dancers.push('')
        }
    }
}
