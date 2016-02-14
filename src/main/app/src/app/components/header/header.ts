import {Component} from 'angular2/core';
import {Menu} from "../menu/menu";
import {UserProfile} from "../userProfile/user-profile";

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
