import {bootstrap} from 'angular2/platform/browser';
import {HTTP_PROVIDERS} from 'angular2/http';
import {ROUTER_PROVIDERS} from 'angular2/router';
import {CurrentUserService} from './app/services/ProfileService';
import {VideoService} from './app/services/VideoService';

import {TangoVideos} from './app/tango-videos';
import {DancerService} from "./app/services/DancerService";
import {SongService} from "./app/services/SongService";

bootstrap(TangoVideos, [
    HTTP_PROVIDERS,
    ROUTER_PROVIDERS,
    CurrentUserService,
    VideoService,
    DancerService,
    SongService
])
    .catch(err => console.error(err));
