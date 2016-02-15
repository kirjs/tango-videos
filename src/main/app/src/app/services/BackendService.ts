import {Http, Response, Headers} from 'angular2/http';
import {Injectable} from "angular2/core";
var YouTubePlayer = require('youtube-player');

interface Dictionary {
    [index: string]: string;
}
@Injectable()
export class BackendService {
    base:string = 'api/';

    read(url: string){
        return this.http.get(this.base + url)
            .map((res) => res.json());
    }

    write(url: string, params: Dictionary){
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');


        return this.http.post('api/login/', JSON.stringify(params), {headers: headers})
            .map(res => res.json());
    }
    constructor(private http:Http){}
}