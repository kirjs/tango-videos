import {Component} from '@angular/core';

import {ROUTER_DIRECTIVES} from '@angular/router';
import {DancerService} from "../../../services/DancerService";

import {MD_CARD_DIRECTIVES} from '@angular2-material/card';
import {MD_BUTTON_DIRECTIVES} from '@angular2-material/button';
import {YoutubeThumb} from "../../common/youtubeThumb/youtubeThumb";

@Component({
    selector: 'dancers',
    template: require('./dancers.html'),
    styles: [require('./dancers.css')],
    providers: [],
    directives: [ROUTER_DIRECTIVES, MD_CARD_DIRECTIVES, MD_BUTTON_DIRECTIVES, YoutubeThumb],
    pipes: []
})
export class Dancers {
    dancers:Array<any> = [];

    fetch() {
        this.dancerService.list().subscribe((dancers)=> {
            this.dancers = dancers.slice(0,10);
        });
    }

    constructor(private dancerService:DancerService) {
        this.fetch();
    }
}
