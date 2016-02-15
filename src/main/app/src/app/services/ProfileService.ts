import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';
import {BackendService} from "./BackendService";


export interface Credentials {
    username: string;
    password: string;
}

@Injectable()
export class CurrentUserService {
    constructor(private http:Http, private backendService: BackendService) {}

    getCurrentUser() {
        return this.backendService.read('currentUser');
    }

    login(credentials:Credentials) {
        return this.backendService.write('login', credentials);
    }

    logout() {
        return this.backendService.read('logout');
    }
}