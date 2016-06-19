import {Injectable} from '@angular/core';
import {Http, URLSearchParams} from '@angular/http';
import 'rxjs/add/operator/map';

const BASE_URL = 'https://www.googleapis.com/youtube/v3/';
const API_TOKEN = 'AIzaSyCW22iBpph-9DMs3rpHa3iXZDpTV0qsLCU';


@Injectable()
export class Youtube {
	constructor(private http: Http){}



	private makeRequest(path: string, params: URLSearchParams ){
		params.append("key", API_TOKEN);
		let url = `${BASE_URL}` + path;
		return this.http.get(url, {search: params})
			.map((res) => res.json());
	}

	getVideo(id: string) {
		let params = new URLSearchParams();
		params.append("id", id);
		params.append("part", "snippet");
		return this.makeRequest("videos",  params)
	}
}
