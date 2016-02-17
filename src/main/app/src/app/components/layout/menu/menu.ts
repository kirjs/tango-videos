import {Component} from 'angular2/core';
import {ROUTER_DIRECTIVES} from 'angular2/router';
import {NeedsPermission} from "../../common/needs-permission/needs-permission";


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
