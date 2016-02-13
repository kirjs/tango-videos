import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';


@Injectable()
export class VideoService {
    constructor(private http:Http) {
    }

    private makeRequest(url) {
        url = '/api/songs' + url;
        return this.http.get(url).map((res) => res.json());
    }

    listNames() {
        return this.makeRequest('/listNames');
    }
    listOrquestras() {
        return this.makeRequest('/listOrquestras');
    }
}