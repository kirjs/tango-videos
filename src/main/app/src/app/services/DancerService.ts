import {Injectable} from 'angular2/core';
import {Http, Response} from 'angular2/http';

import {Observable} from 'rxjs';


@Injectable()
export class DancerService {
    constructor(private http:Http) {
    }

    private makeRequest(url) {
        url = '/api/dancers' + url;
        return this.http.get(url).map((res) => res.json());
    }

    get(id:String) {
        return this.makeRequest(`/${id}`);
    }

    list() {
        return this.makeRequest(`/list`);
    }
}