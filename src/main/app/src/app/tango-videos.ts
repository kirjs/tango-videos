import {Component} from 'angular2/core';
import {Router, Route, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {Home} from './components/home/home';
import {Videos} from './components/videos/videos';
import {VideoForm} from './components/videoForm/videoForm';
import {UserProfile} from './components/userProfile/userProfile';
import {Dancer} from "./components/dancer/dancer";
import {Explore} from "./components/explore/explore";



@Component({
    selector: 'tango-videos',
    providers: [],
    template: require('./tango-videos.html'),
    directives: [ROUTER_DIRECTIVES.concat(UserProfile)],
    pipes: []
})
@RouteConfig([
    new Route({path: '/videos', component: Explore, name: 'Videos', useAsDefault: true}),
    new Route({path: '/dancers/:id', component: Dancer, name: 'Dancers'}),
    new Route({path: '/home', component: Home, name: 'Home'}),
    new Route({path: '/new/video', component: VideoForm, name: 'AddVideo'}),
])
export class TangoVideos {
    constructor() {

    }
}
