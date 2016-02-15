import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';

import {Observable} from 'rxjs';
import {BackendService} from "./BackendService";


@Injectable()
export class DancerService {
    constructor(private backendService: BackendService) {
    }

    get(id:String) {
        return this.backendService.read(`dancers/${id}`)
    }

    listNames() {
        return this.backendService.read('dancers/listNames');
    }

    list() {
        return this.backendService.read('dancers/list');
    }

}