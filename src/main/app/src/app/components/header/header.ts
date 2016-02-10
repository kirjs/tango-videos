import {Component} from 'angular2/core';
import {Menu} from "../menu/menu";

@Component({
    selector: 'header',
    template: require('./header.html'),
    styles: [require('./header.css')],
    providers: [],
    directives: [Menu],
    pipes: []
})
export class Header {

    constructor() {
    }

}
