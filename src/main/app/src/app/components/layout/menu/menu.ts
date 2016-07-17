import {Component} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router';
import {NeedsPermission} from "../../common/needsPermission/needsPermission";


@Component({
    selector: 'menu',
    template: require('./menu.html'),
    styles: [require('./menu.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, NeedsPermission],
    pipes: []
})
export class Menu {

    constructor() {
    }

}
