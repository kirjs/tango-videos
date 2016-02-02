import {Component, Input, ViewChildren, Output, EventEmitter} from 'angular2/core';

@Component({
    selector: 'dancers',
    templateUrl: 'app/components/dancers/dancers.html',
    styleUrls: ['app/components/dancers/dancers.css'],
    providers: [],
    directives: [],
    pipes: []
})
export class Dancers {
    @Input() dancers:any = [];
    @Output() addCallback = new EventEmitter();
    saving: boolean = false;

    constructor() {
    }

    save(dancer: String) {
        this.addCallback.emit(dancer);
    }

    add() {
        this.dancers = this.dancers || [];
        if (this.dancers.filter(i => i == '').length == 0) {
            this.dancers.push('')
        }
    }
}
