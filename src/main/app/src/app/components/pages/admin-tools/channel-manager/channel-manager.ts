import {Component} from 'angular2/core';
import {AddableField} from "../../../common/addable-field/addable-field";

@Component({
    selector: 'channel-manager',
    template: require('./channel-manager.html'),
    styles: [require('./channel-manager.css')],
    providers: [],
    directives: [AddableField],
    pipes: []
})
export class ChannelManager {

    constructor() {
    }

}
