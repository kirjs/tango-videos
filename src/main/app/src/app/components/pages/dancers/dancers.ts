import {Component} from '@angular/core';

import {ROUTER_DIRECTIVES} from '@angular/router';
import {DancerService} from "../../../services/DancerService";

import {MD_CARD_DIRECTIVES} from '@angular2-material/card';
import {MD_BUTTON_DIRECTIVES} from '@angular2-material/button';
import {MD_GRID_LIST_DIRECTIVES} from '@angular2-material/grid-list';

@Component({
    selector: 'dancers',
    template: require('./dancers.html'),
    styles: [require('./dancers.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, MD_CARD_DIRECTIVES, MD_BUTTON_DIRECTIVES, MD_GRID_LIST_DIRECTIVES],
    pipes: []
})
export class Dancers {
    dancers:Array<any> = [];

    fetch() {
        this.dancerService.list().subscribe((dancers)=> {
            this.dancers = dancers;
        });
    }

    constructor(private dancerService:DancerService) {
        this.fetch();
    }
}
