import {Component} from 'angular2/core';
import {Router, Route, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {Home} from './components/home/home';
import {VideoForm} from './components/videoForm/videoForm';
import {RepoBrowser} from './components/repo-browser/repo-browser';

@Component({
  selector: 'tango-videos',
  providers: [],
  templateUrl: 'app/tango-videos.html',
  directives: [ROUTER_DIRECTIVES],
  pipes: []
})
@RouteConfig([
  new Route({ path: '/home', component: Home, name: 'Home', useAsDefault: true}),
  new Route({ path: '/new/video', component: VideoForm, name: 'AddVideo'}),
  new Route({ path: '/github/...', component: RepoBrowser, name: 'RepoBrowser'})
])
export class TangoVideos {

  constructor() {}

}
