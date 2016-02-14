import {Component, Input} from 'angular2/core';

@Component({
    selector: 'icon',
    template: require('./icon.html'),
    styles: [require('./icon.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class Icon {
    @Input() name: String;

    constructor() {
    }

}
