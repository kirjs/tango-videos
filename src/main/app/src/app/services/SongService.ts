import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';
import {BackendService} from "./BackendService";


@Injectable()
export class SongService {
    constructor(private backendService: BackendService) {}

    listNames() {
        return this.backendService.read('listNames');
    }
    listOrquestras() {
        return this.backendService.read('listOrquestras');
    }
}