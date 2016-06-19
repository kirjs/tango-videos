import {Component} from '@angular/core';

import {ROUTER_DIRECTIVES} from '@angular/router';
import {DancerService} from "../../../services/DancerService";

@Component({
    selector: 'dancers',
    template: require('./dancers.html'),
    styles: [require('./dancers.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES],
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
