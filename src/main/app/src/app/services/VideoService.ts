import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';



@Injectable()
export class VideoService {
	constructor(private http: Http){}

	private makeRequest(url){
		let url = '/api/videos' + url;
		return this.http.get(url).map((res) => res.json());
	}

	list() {
        return this.makeRequest("/list")
	}
}