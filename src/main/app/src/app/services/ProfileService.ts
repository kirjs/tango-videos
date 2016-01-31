import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';


export interface Credentials {
    username: string;
    password: string;
}

@Injectable()
export class CurrentUserService {
	constructor(private http: Http){}
	
	getCurrentUser(){
		return this.makeRequest();
	}

	private makeRequest(){
		let url = `/api/currentUser`;
		return this.http.get(url).map((res) => res.json());
	}

	login(credentials:Credentials) {
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');
        var creds = "username=" + credentials.username + "&password=" + credentials.password;

        return this.http.post('api/login/', creds, {headers: headers}).map(res => res.json());
	}
}