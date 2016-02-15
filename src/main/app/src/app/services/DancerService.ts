import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';

import {Observable} from 'rxjs';
import {BackendService} from "./BackendService";


@Injectable()
export class DancerService {
    constructor(private http:Http, private backendService: BackendService) {
    }

    private makeRequest(url) {
        url = '/api/dancers' + url;
        return this.http.get(url).map((res) => res.json());
    }

    get(id:String) {
        return this.backendService.read(`dancers/${id}`)
    }

    listNames() {
        return this.backendService.read('dancers/listNames');
    }

    list() {
        return this.backendService.read('dancers/listNames');
    }

}