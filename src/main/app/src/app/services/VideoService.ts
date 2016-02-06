import {Injectable} from 'angular2/core';
import {Http, URLSearchParams, Headers} from 'angular2/http';
import 'rxjs/add/operator/map';


@Injectable()
export class VideoService {
    constructor(private http:Http) {
    }

    private makeRequest(url) {
        url = '/api/videos' + url;
        return this.http.get(url).map((res) => res.json());
    }

    list() {
        return this.makeRequest("/list")
    }

    addDancer(id:String, dancer:String) {
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post(`api/videos/${id}/dancers/add`,
            'name=' + dancer, {headers: headers}).map(res => res.json());
    }

    removeDancer(id:String, dancer:String):any {
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post(`api/videos/${id}/dancers/remove`,
            'name=' + dancer, {headers: headers}).map(res => res.json());

    }

    updateSongInfo(id:any, index:Number, field:String, data:String) {
        var headers = new Headers();
        headers.append('Content-Type', 'application/x-www-form-urlencoded');

        return this.http.post(`api/videos/${id}/songs/update`,
            'index=' + index + '&field='+field + '&data=' + data , {headers: headers}).map(res => res.json());

    }


}