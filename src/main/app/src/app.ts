import {bootstrap} from 'angular2/platform/browser';
import {HTTP_PROVIDERS} from 'angular2/http';
import {ROUTER_PROVIDERS} from 'angular2/router';

import {TangoVideos} from './app/tango-videos';

bootstrap(TangoVideos, [HTTP_PROVIDERS, ROUTER_PROVIDERS])
  .catch(err => console.error(err));
