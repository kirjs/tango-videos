import {Component} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';

@Component({
    selector: 'menu',
    template: require('./menu.html'),
    styles: [require('./menu.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
    pipes: []
})
export class Menu {

    constructor() {
    }

}
