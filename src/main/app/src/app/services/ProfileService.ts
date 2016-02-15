import {Injectable} from 'angular2/core';
import {BackendService} from "./BackendService";
import {Observable} from "rxjs";


export interface Credentials {
    username: string;
    password: string;
}

@Injectable()
export class CurrentUserService {
    constructor(private backendService: BackendService) {}

    getCurrentUser():Observable<any> {
        return this.backendService.read('currentUser');
    }

    login(credentials:Credentials):Observable<any> {
        return this.backendService.write('login', credentials);
    }

    logout():Observable<any> {
        return this.backendService.read('logout');
    }
}