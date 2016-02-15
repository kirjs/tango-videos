import {Http, Response, Headers} from 'angular2/http';
import {Injectable} from "angular2/core";
import {Observable, Subject} from "rxjs";

@Injectable()
export class BackendService {
    private base:string = 'api/';
    public progress;

    read(url:string):Observable<any> {
        return this.http.get(this.base + url)
            .map((res) => res.json());
    }

    write(url:string, params:any):Observable<any> {
        var headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');
        var requestProgress = new Subject().startWith('Starting a request');
        this.progress.next(requestProgress);


        return this.http.post(this.base + url, JSON.stringify(params), {headers: headers})
            .map(res => res.json())
            .retryWhen(attempts => attempts.flatMap(()=> {
                return Observable.create((obs)=> {
                    if (prompt("startAgain?")) {
                        obs.next();
                    } else {
                        obs.complete();
                    }
                })
            }))
    }

    constructor(private http:Http) {
        this.progress = new Subject();
    }
}
