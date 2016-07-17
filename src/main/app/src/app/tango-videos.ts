import {Component} from '@angular/core';
import {ROUTER_DIRECTIVES} from '@angular/router';

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
export class TangoVideos {
    constructor() {

    }
}
