import {Component} from 'angular2/core';
import {Router, Route, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {Home} from './components/home/home';
import {Videos} from './components/videos/videos';
import {VideoForm} from './components/videoForm/videoForm';
import {UserProfile} from './components/userProfile/userProfile';
import {Dancers} from "./components/dancers/dancers";


@Component({
    selector: 'tango-videos',
    providers: [],
    templateUrl: 'app/tango-videos.html',
    directives: [ROUTER_DIRECTIVES.concat(UserProfile)],
    pipes: []
})
@RouteConfig([
    new Route({path: '/videos', component: Videos, name: 'Videos', useAsDefault: true}),
    new Route({path: '/dancers/:id', component: Dancers, name: 'Dancers'}),
    new Route({path: '/home', component: Home, name: 'Home'}),
    new Route({path: '/new/video', component: VideoForm, name: 'AddVideo'}),
])
export class TangoVideos {

    constructor() {

    }

}
