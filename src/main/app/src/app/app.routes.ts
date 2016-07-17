///<reference path="components/pages/needs-review/needs-review.ts"/>
import { provideRouter, RouterConfig, Route } from '@angular/router';
import {AdminTools} from "./components/pages/admin-tools/admin-tools";
import {NeedsReview} from "./components/pages/needs-review/needs-review";
import {VideoForm} from "./components/entities/video/video-form/video-form";
import {Home} from "./components/pages/home/home";
import {TagSongs} from "./components/pages/tag-songs/tag-songs";
import {Music} from "./components/pages/music/music";
import {Dancer} from "./components/entities/dancer/dancer/dancer";
import {Dancers} from "./components/pages/dancers/dancers";
import {Explore} from "./components/pages/explore/explore";

import { Type } from '@angular/core';

const routes:RouterConfig = [
    {path: 'videos', component: Explore},
    {path: 'dancers', component: Dancers},
    {path: 'dancers/:id', component: Dancer},
    {path: 'music', component: Music},
    {path: 'tag', component: TagSongs},
    {path: 'home', component: Home},
    {path: 'new/video', component: VideoForm},
    {path: 'needs-review', component: NeedsReview},
    {path: 'admin-tools', component: AdminTools}
];

export const appRouterProviders = [
    provideRouter(routes)
];