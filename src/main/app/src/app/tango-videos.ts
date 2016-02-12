import {Component} from 'angular2/core';
import {Router, Route, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {Home} from './components/home/home';
import {Videos} from './components/videos/videos';
import {VideoForm} from './components/videoForm/videoForm';
import {UserProfile} from './components/userProfile/userProfile';
import {Dancer} from "./components/dancer/dancer";
import {Explore} from "./components/explore/explore";
import {NeedsReview} from "./components/needs-review/needs-review";
import {Header} from "./components/header/header";
import {Dancers} from "./components/dancers/dancers";



@Component({
    selector: 'tango-videos',
    providers: [],
    template: require('./tango-videos.html'),
    directives: [ROUTER_DIRECTIVES, UserProfile, Header],
    pipes: []
})
@RouteConfig([
    new Route({path: '/videos', component: Explore, name: 'Videos', useAsDefault: true}),
    new Route({path: '/dancers', component: Dancers, name: 'Dancers'}),
    new Route({path: '/dancers/:id', component: Dancer, name: 'Dancer'}),
    new Route({path: '/home', component: Home, name: 'Home'}),
    new Route({path: '/new/video', component: VideoForm, name: 'AddVideo'}),
    new Route({path: '/needs-review', component: NeedsReview, name: 'NeedsReview'}),
])
export class TangoVideos {
    constructor() {

    }
}
