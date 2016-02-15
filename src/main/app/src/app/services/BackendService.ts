import {Http, Response, Headers} from 'angular2/http';
import {Injectable} from "angular2/core";
import {Observable, Subject} from "rxjs";

function errorToMessage(response, request$, retry$) {
    var actions = {
        retry: {
            label: 'Try again',
            callback: () => {
                retry$.next();
            }
        },
        login: {
            label: 'Login',
            callback: () => {
                throw new Error("Not implemented");
            }
        },
        cancel:  {
            label: 'Cancel',
            callback: () => {
                request$.complete();
            }
        }
    };

    var messages = {
        "org.apache.shiro.authz.UnauthorizedException": {
            message: 'You need to be authorized for this action',
            actions: [ actions.cancel]
        },
        "default": {
            message: 'Request failed',
            actions: [actions.retry,  actions.cancel]
        }
    };
    return messages[response.name] || messages.default;
}

@Injectable()
export class BackendService {
    private base:string = 'api/';
    public progress = new Subject();

    read(url:string):Observable<any> {
        return this.trackProgress(this.http.get(this.base + url)
            .map((res) => res.json()), url);
    }

    trackProgress(request, url){
        var requestProgress = new Subject();
        this.progress.next(requestProgress);



        return request
            .do(res => requestProgress.complete())
            .retryWhen(attempts => attempts.flatMap((response)=> {
            return Observable.create(function (obs) {
                var error;
                try{
                    error = response.json();
                } catch ( e ){
                    error = {
                        name: 'ServerError'
                    };
                }
                requestProgress.next(errorToMessage(error, requestProgress, obs));
            })
        }))


    }

    write(url:string, params:any):Observable<any> {
        var headers = new Headers();
        headers.append('Content-Type', 'application/json');
        headers.append('Accept', 'application/json');

        return this.trackProgress(this.http.post(this.base + url, JSON.stringify(params), {headers: headers})
            .map(res => res.json()), url);


    }

    constructor(private http:Http) {

    }
}
