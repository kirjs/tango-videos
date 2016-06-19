import {Injectable} from '@angular/core';
import {BackendService} from "./BackendService";
import {Observable, Subject, BehaviorSubject} from "rxjs";


export interface Credentials {
    username: string;
    password: string;
}

@Injectable()
export class CurrentUserService {

    private permissions:Subject<any>;
    public permissionObservable:BehaviorSubject<any>;

    constructor(private backendService:BackendService) {
        this.permissionObservable = new BehaviorSubject({});
        this.permissions = new Subject();
        this.permissions
            .startWith('')
            .flatMap(() => this.fetchPermissions())
            .map((permissions:Array<string>)=>
                permissions.length ?
                    permissions.reduce((result, permission)=> {
                        result[permission] = true;
                        return result;
                    }, {}) :
                {loggedOut: true}
            )
            .subscribe((data)=> {
                this.permissionObservable.next(data);
            });

    }

    refreshPermissions() {
        this.permissions.next(true);
    }

    getCurrentUser():Observable<any> {
        return this.backendService.read('currentUser').do(this.refreshPermissions.bind(this));
    }

    fetchPermissions():Observable<Array<String>> {
        return this.backendService.read('currentUser/permissions');
    }

    login(credentials:Credentials):Observable<any> {
        return this.backendService.write('login', credentials).do(this.refreshPermissions.bind(this));
    }

    logout():Observable<any> {
        return this.backendService.read('logout').do(this.refreshPermissions.bind(this));
    }
}
