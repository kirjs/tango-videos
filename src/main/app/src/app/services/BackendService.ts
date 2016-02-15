import {Http, Response} from 'angular2/http';
import {Injectable} from "angular2/core";
var YouTubePlayer = require('youtube-player');

@Injectable()
export class BackendService {
    base:String = 'api/';
    read(url: String){
        return this.http.get(this.base + url)
            .map((res) => res.json());
    }
    constructor(private http:Http){}
}