import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable, Subject} from "rxjs";


export interface Credentials {
    username: string;
    password: string;
}

@Injectable()
export class CurrentUserService {

    permissions: Subject;
    private permissionObservable: Observable;
    constructor(private backendService: BackendService) {
        this.permissions = new Subject();
        this.permissionObservable = this.permissions
            .startWith('')
            .flatMap(() => this.fetchPermissions())
            .share()
    }

    refreshPermissions(){

    }

    getCurrentUser():Observable<any> {
        return this.backendService.read('currentUser');
    }

    fetchPermissions():Observable<any> {
        return this.backendService.read('user/permissions');
    }

    login(credentials:Credentials):Observable<any> {
        return this.backendService.write('login', credentials);
    }

    logout():Observable<any> {
        return this.backendService.read('logout');
    }
}
