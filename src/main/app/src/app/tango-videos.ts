import {Component} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router';

import {VideoPlayer} from './components/entities/player/video-player/video-player';
import {RequestStatus} from './components/common/request-status/request-status';
import {MD_TOOLBAR_DIRECTIVES} from '@angular2-material/toolbar';
import {Menu} from "./components/layout/menu/menu";
import {UserProfile} from "./components/layout/user-profile/user-profile";

@Component({
    selector: 'tango-videos',
    providers: [],
    template: require('./tango-videos.html'),
    directives: [ROUTER_DIRECTIVES, VideoPlayer, RequestStatus,
        MD_TOOLBAR_DIRECTIVES, Menu, UserProfile],
    pipes: []
})
export class TangoVideos {
    constructor() {

    }
}
