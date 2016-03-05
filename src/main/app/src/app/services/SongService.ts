import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable} from "rxjs";


@Injectable()
export class SongService {
    constructor(private backendService:BackendService) {
    }

    listNames():Observable<any> {
        return this.backendService.read('songs/listNames');
    }

    listOrquestras():Observable<any> {
        return this.backendService.read('songs/listOrquestras');
    }

    list():any {
        return this.backendService.read('songs/list');
    }
}