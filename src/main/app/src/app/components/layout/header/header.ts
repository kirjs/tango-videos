import {Component} from 'angular2/core';
import {UserProfile} from "../user-profile/user-profile";
import {Menu} from "../menu/menu";

@Component({
    selector: 'header',
    template: require('./header.html'),
    styles: [require('./header.css')],
    providers: [],
    directives: [Menu, UserProfile],
    pipes: []
})
export class Header {

    constructor() {
    }

}
