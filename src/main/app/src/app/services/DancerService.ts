import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';



@Injectable()
export class DancerService {
	constructor(private http: Http){}

	private makeRequest(url){
		url = '/api/dancers' + url;
		return this.http.get(url).map((res) => res.json());
	}

	get(id: String) {
		return this.makeRequest(`/${id}`);
	}
}