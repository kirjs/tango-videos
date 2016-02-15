import {Http, Response, Headers} from 'angular2/http';
import {Injectable} from "angular2/core";
import {Observable} from "rxjs";

@Injectable()
export class BackendService {
    private base:string = 'api/';

    read(url:string):Observable<any> {
        return this.http.get(this.base + url)
            .map((res) => res.json());
    }

    write(url:string, params:any):Observable<any> {
        var headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');

        return this.http.post(this.base + url, JSON.stringify(params), {headers: headers})
            .map(res => res.json());
    }

    constructor(private http:Http) {
    }
}