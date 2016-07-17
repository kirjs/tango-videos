import {Component} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router';
import {NeedsPermission} from "../../common/needsPermission/needsPermission";
import {MD_BUTTON_DIRECTIVES} from '@angular2-material/button';


@Component({
    selector: 'menu',
    template: require('./menu.html'),
    styles: [require('./menu.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, NeedsPermission, MD_BUTTON_DIRECTIVES],
    pipes: []
})
export class Menu {

    constructor() {
    }

}
