import {Component} from 'angular2/core';

@Component({
    selector: 'empty',
    template: require('./empty.html'),
    styles: [require('./empty.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class Home {

    constructor() {
    }

}
