import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable} from "rxjs";

@Injectable()
export class ChannelService {
    constructor(private backendService: BackendService) {}

    add(id:String):Observable<any> {
        return this.backendService.write('channels/add', {id: id});
    }

    list():Observable<any> {
        return this.backendService.read('channels/list');
    }

    fetchLatestVideos(id:String):Observable<any> {
        return this.backendService.write('channels/fetchLatestVideos', {id: id});
    }
}
