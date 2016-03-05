import {Component} from 'angular2/core';
import {Router, Route, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {Home} from './components/pages/home/home';
import {Videos} from './components/entities/video/videos/videos';
import {VideoForm} from './components/entities/video/video-form/video-form';
import {Dancer} from './components/entities/dancer/dancer/dancer';
import {Explore} from './components/pages/explore/explore';
import {NeedsReview} from './components/pages/needs-review/needs-review';
import {Header} from './components/layout/header/header';
import {Dancers} from './components/pages/dancers/dancers';
import {VideoPlayer} from './components/entities/player/video-player/video-player';
import {RequestStatus} from './components/common/request-status/request-status';
import {AdminTools} from './components/pages/admin-tools/admin-tools';
import {TagSongs} from './components/pages/tag-songs/tag-songs';
import {Music} from "./components/pages/music/music";


@Component({
    selector: 'tango-videos',
    providers: [],
    template: require('./tango-videos.html'),
    directives: [ROUTER_DIRECTIVES, Header, VideoPlayer, RequestStatus],
    pipes: []
})
@RouteConfig([
    new Route({path: '/videos', component: Explore, name: 'Videos', useAsDefault: true}),
    new Route({path: '/dancers', component: Dancers, name: 'Dancers'}),
    new Route({path: '/dancers/:id', component: Dancer, name: 'Dancer'}),
    new Route({path: '/music', component: Music, name: 'Music'}),
    new Route({path: '/tag', component: TagSongs, name: 'Tag'}),
    new Route({path: '/home', component: Home, name: 'Home'}),
    new Route({path: '/new/video', component: VideoForm, name: 'AddVideo'}),
    new Route({path: '/needs-review', component: NeedsReview, name: 'NeedsReview'}),
    new Route({path: '/admin-tools', component: AdminTools, name: 'AdminTools'}),
])
export class TangoVideos {
    constructor() {

    }
}
