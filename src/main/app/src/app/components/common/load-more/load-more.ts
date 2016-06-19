import {Component, Input} from '@angular/core';

@Component({
    selector: '[load-more]',
    template: require('./load-more.html'),
    styles: [require('./load-more.css')],
    providers: [],
    directives: [],
    pipes: []
})
export class LoadMore {
    @Input() loader: Function;
    constructor() {
    }

}
