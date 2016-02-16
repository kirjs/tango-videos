import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable, Subject} from "rxjs";


export interface Credentials {
    username: string;
    password: string;
}

@Injectable()
export class CurrentUserService {

    private permissions:Subject;
    public permissionObservable:Observable;

    constructor(private backendService:BackendService) {
        this.permissions = new Subject();
        this.permissionObservable = this.permissions
            .startWith('')
            .flatMap(() => this.fetchPermissions())
            .map((permissions)=>permissions.reduce((result, permission)=> {
                    result[permission] = true;
                    return result;
                }, {})
            )
            .share();
    }

    refreshPermissions() {
        this.permissions.next(true);
    }

    getCurrentUser():Observable<any> {
        return this.backendService.read('currentUser').do(this.refreshPermissions.bind(this));
    }

    fetchPermissions():Observable<any> {
        return this.backendService.read('currentUser/permissions');
    }

    login(credentials:Credentials):Observable<any> {
        return this.backendService.write('login', credentials).do(this.refreshPermissions.bind(this));
    }

    logout():Observable<any> {
        return this.backendService.read('logout').do(this.refreshPermissions.bind(this));
    }
}
