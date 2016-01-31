import {Component} from 'angular2/core';
import {Router, Route, RouteConfig, ROUTER_DIRECTIVES} from 'angular2/router';

import {Home} from './components/home/home';
import {VideoForm} from './components/videoForm/videoForm';
import {UserProfile} from './components/userProfile/userProfile';


@Component({
  selector: 'tango-videos',
  providers: [],
  templateUrl: 'app/tango-videos.html',
  directives: [ROUTER_DIRECTIVES.concat(UserProfile)],
  pipes: []
})
@RouteConfig([
  new Route({ path: '/home', component: Home, name: 'Home', useAsDefault: true}),
  new Route({ path: '/new/video', component: VideoForm, name: 'AddVideo'}),

])
export class TangoVideos {

  constructor() {

  }

}
