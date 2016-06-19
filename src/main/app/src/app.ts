import { bootstrap } from '@angular/platform-browser-dynamic';
import {disableDeprecatedForms, provideForms} from '@angular/forms';
import {HTTP_PROVIDERS} from '@angular/http';
import {ROUTER_PROVIDERS} from '@angular/router';
import {CurrentUserService} from './app/services/ProfileService';
import {VideoService} from './app/services/VideoService';

import {TangoVideos} from './app/tango-videos';
import {DancerService} from "./app/services/DancerService";
import {SongService} from "./app/services/SongService";
import {PlayerService} from "./app/services/PlayerService";
import {BackendService} from "./app/services/BackendService";
import {ChannelService} from "./app/services/ChannelService";

bootstrap(TangoVideos, [
    disableDeprecatedForms(),
    HTTP_PROVIDERS,
    ROUTER_PROVIDERS,
    BackendService,
    CurrentUserService,
    VideoService,
    DancerService,
    SongService,
    PlayerService,
    ChannelService
])
    .catch(err => console.error(err));
